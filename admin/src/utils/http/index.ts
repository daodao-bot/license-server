import Axios, {
  type AxiosInstance,
  type AxiosRequestConfig,
  type CustomParamsSerializer
} from "axios";
import type {
  PureHttpError,
  PureHttpRequestConfig,
  PureHttpResponse,
  RequestMethods
} from "./types.d";
import { stringify } from "qs";
import NProgress from "../progress";
import { formatToken, getToken, removeToken } from "@/utils/auth";

import { ElNotification } from "element-plus";

import { v4 as uuid } from "uuid";

import license from "@/utils/license";
import router from "@/router";
import { useConfigStoreHook } from "@/store/modules/config";
import type { ConfigData } from "@/api/admin";

// 相关配置请参考：www.axios-js.com/zh-cn/docs/#axios-request-config-1
const defaultConfig: AxiosRequestConfig = {
  // baseURL: license.SERVER,
  baseURL: "/server",
  // 请求超时时间
  timeout: 10000,
  headers: {
    Accept: "application/json, text/plain, */*",
    "Content-Type": "application/json",
    "X-Requested-With": "XMLHttpRequest"
  },
  // 数组格式参数序列化（https://github.com/axios/axios/issues/5142）
  paramsSerializer: {
    serialize: stringify as unknown as CustomParamsSerializer
  }
};

class PureHttp {
  constructor() {
    this.httpInterceptorsRequest();
    this.httpInterceptorsResponse();
  }

  /** `token`过期后，暂存待执行的请求 */
  private static requests = [];

  /** 防止重复刷新`token` */
  private static isRefreshing = false;

  /** 初始化配置对象 */
  private static initConfig: PureHttpRequestConfig = {};

  /** 保存当前`Axios`实例对象 */
  private static axiosInstance: AxiosInstance = Axios.create(defaultConfig);

  /** 重连原始请求 */
  private static retryOriginalRequest(config: PureHttpRequestConfig) {
    return new Promise(resolve => {
      PureHttp.requests.push((token: string) => {
        config.headers["Authorization"] = formatToken(token);
        resolve(config);
      });
    });
  }

  /** 请求拦截 */
  private httpInterceptorsRequest(): void {
    PureHttp.axiosInstance.interceptors.request.use(
      async (config: PureHttpRequestConfig): Promise<any> => {
        // 开启进度条动画
        NProgress.start();
        // 优先判断post/get等方法是否传入回调，否则执行初始化设置等回调
        if (typeof config.beforeRequestCallback === "function") {
          config.beforeRequestCallback(config);
          return config;
        }
        if (PureHttp.initConfig.beforeRequestCallback) {
          PureHttp.initConfig.beforeRequestCallback(config);
          return config;
        }

        console.log("url", config.url ?? "");

        const trace = uuid().replaceAll("-", "");
        const time = new Date().toUTCString();
        if (!config.headers) {
          config.headers = {};
        }

        let doSecurity = true;

        const noSecurityUri: string[] = license.NO_SECURITY_URI;
        const noSecurity: boolean = noSecurityUri.some(url =>
          config.url.endsWith(url)
        );
        let apiSecurity = true;
        if (!noSecurity) {
          apiSecurity = (await useConfigStoreHook().getConfig()).apiSecurity;
        }
        if (noSecurity || !apiSecurity) {
          doSecurity = false;
        }

        if (doSecurity) {
          const configData: ConfigData = useConfigStoreHook().config;
          config.headers["X-App-Id"] = configData.appId;
          config.headers["X-Security"] = "AES";
        }

        config.headers["X-Time"] = time;
        config.headers["X-Trace"] = trace;

        if (config.method === "post" || config.method === "POST") {
          const { param } = config.data;
          console.log("param", param);
          if (doSecurity) {
            const configData: ConfigData = useConfigStoreHook().config;
            const plains: string = JSON.stringify(param);
            config.data.param = license.encrypt(configData.license, plains);
          }
        }

        /** 请求白名单，放置一些不需要`token`的接口（通过设置请求白名单，防止`token`过期后再请求造成的死循环问题） */
        const whiteList = license.NO_AUTHORIZATION_URI;
        return whiteList.some(url => config.url.endsWith(url))
          ? config
          : new Promise(resolve => {
              const token = getToken();
              if (token) {
                if (config && config.headers) {
                  config.headers["Authorization"] = formatToken(token);
                }
                resolve(config);
              } else {
                resolve(config);
              }
            });
      },
      error => {
        return Promise.reject(error);
      }
    );
  }

  /** 响应拦截 */
  private httpInterceptorsResponse(): void {
    const instance = PureHttp.axiosInstance;
    instance.interceptors.response.use(
      (response: PureHttpResponse) => {
        const $config = response.config;
        // 关闭进度条动画
        NProgress.done();
        // 优先判断post/get等方法是否传入回调，否则执行初始化设置等回调
        if (typeof $config.beforeResponseCallback === "function") {
          $config.beforeResponseCallback(response);
          return response.data;
        }
        if (PureHttp.initConfig.beforeResponseCallback) {
          PureHttp.initConfig.beforeResponseCallback(response);
          return response.data;
        }

        const noSecurityUri: string[] = license.NO_SECURITY_URI;
        const noSecurity: boolean = noSecurityUri.some(url =>
          $config.url.endsWith(url)
        );
        let apiSecurity = true;
        let configData: ConfigData;
        if (!noSecurity) {
          configData = useConfigStoreHook().config;
          apiSecurity = configData.apiSecurity;
        }
        const doSecurity = !(noSecurity || !apiSecurity);

        if ($config.method === "post" || $config.method === "POST") {
          if (doSecurity) {
            const cipher = response.data.data;
            const plains = license.decrypt(configData.license, cipher);
            const data = JSON.parse(plains);
            console.log("data", data);
            response.data.data = data;
          } else {
            console.log("data", response.data.data);
          }
        }
        const code = response.data.code;
        const message = response.data.message;

        if (code) {
          if (code !== "000000") {
            ElNotification({
              title: "Error",
              message: message,
              type: "error"
            });
            if (code === "111111") {
              removeToken();
              router.push("/login").then(r => {
                console.debug(r);
              });
            }
            return Promise.reject(response);
          } else {
            // console.debug("message", message);
          }
        }

        return response.data;
      },
      (error: PureHttpError) => {
        const $error = error;
        $error.isCancelRequest = Axios.isCancel($error);
        // 关闭进度条动画
        NProgress.done();
        // 所有的响应异常 区分来源为取消请求/非取消请求
        return Promise.reject($error);
      }
    );
  }

  /** 通用请求工具函数 */
  public request<T>(
    method: RequestMethods,
    url: string,
    param?: AxiosRequestConfig,
    axiosConfig?: PureHttpRequestConfig
  ): Promise<T> {
    const config = {
      method,
      url,
      ...param,
      ...axiosConfig
    } as PureHttpRequestConfig;

    // 单独处理自定义请求/响应回调
    return new Promise((resolve, reject) => {
      PureHttp.axiosInstance
        .request(config)
        .then((response: undefined) => {
          resolve(response);
        })
        .catch(error => {
          reject(error);
        });
    });
  }

  /** 单独抽离的`post`工具函数 */
  public post<I, O>(
    url: string,
    params?: AxiosRequestConfig<I>,
    config?: PureHttpRequestConfig
  ): Promise<O> {
    return this.request<O>("post", url, params, config);
  }

  /** 单独抽离的`get`工具函数 */
  public get<I, O>(
    url: string,
    params?: AxiosRequestConfig<I>,
    config?: PureHttpRequestConfig
  ): Promise<O> {
    return this.request<O>("get", url, params, config);
  }
}

export const http = new PureHttp();
