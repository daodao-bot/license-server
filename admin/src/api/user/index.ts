import { http } from "@/utils/http";
import type { No, Ok, Request, Response } from "@/api";

export type LoginParam = {
  /** username */
  username: string;
  /** password */
  password: string;
};

export type UserData = {
  /** username */
  username: string;
};

export type TokenData = {
  /** token */
  token: string;
};

/** 登录 传入 username & password 信息，获取 token POST /api/user/login */
export async function userLogin(
  request: Request<LoginParam>
): Promise<Response<TokenData>> {
  return http.post<Request<LoginParam>, Response<TokenData>>(
    `/api/user/login`,
    {
      data: request
    }
  );
}

/** 登录 header 中传入 token 信息，获取用户信息 POST /api/user/info */
export async function userInfo(
  request: Request<No>
): Promise<Response<UserData>> {
  return http.post<Request<No>, Response<UserData>>(`/api/user/info`, {
    data: request
  });
}

/** 退出 header 中传入 token 信息，退出登录 POST /api/user/logout */
export async function userLogout(request: Request<No>): Promise<Response<Ok>> {
  return http.post<Request<No>, Response<Ok>>(`/api/user/logout`, {
    data: request
  });
}
