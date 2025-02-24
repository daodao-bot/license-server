import type { No, Request, Response } from "@/api";
import { http } from "@/utils/http";

export type ConfigData = {
  /** appId */
  appId: string;
  /** license */
  license: string;
  /** apiSecurity */
  apiSecurity: boolean;
};

/** 管理平台配置 header 中传入 token 信息，获取管理平台配置 POST /api/admin/config */
export async function adminConfig(
  request: Request<No>
): Promise<Response<ConfigData>> {
  return http.post<Request<No>, Response<ConfigData>>(`/api/admin/config`, {
    data: request
  });
}
