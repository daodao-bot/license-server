import { http } from "@/utils/http";
import type { IdParam, PageData, PageParam, Request, Response } from "@/api";

export type LicenseData = {
  /** id ID */
  id: string;
  /** productId 产品 id */
  productId: string;
  /** customerId 客户 id */
  customerId: string;
  /** appId 应用 id */
  appId: string;
  /** license 许可证（授权码） */
  license: string;
  /** periodStart 有效期开始 */
  periodStart: string;
  /** periodEnd 有效期结束 */
  periodEnd?: string;
  /** longTerm 是否长期有效 */
  longTerm: boolean;
  /** createTime 创建时间 */
  createTime: string;
  /** updateTime 更新时间 */
  updateTime: string;
  /** valid 是否有效 */
  valid: boolean;
};

export type LicenseSearch = {
  /** productId 产品 id */
  productId?: string;
  /** customerId 客户 id */
  customerId?: string;
  /** appId 应用 id */
  appId?: string;
  /** license 许可证（授权码） */
  license?: string;
  /** periodStart 有效期开始 */
  periodStart?: string;
  /** periodEnd 有效期结束 */
  periodEnd?: string;
  /** longTerm 是否长期有效 */
  longTerm?: boolean;
  /** valid 是否有效 */
  valid?: boolean;
};

export type LicenseUpsert = {
  /** id ID */
  id?: string;
  /** productId 产品 id */
  productId: string;
  /** customerId 客户 id */
  customerId: string;
  /** appId 应用 id */
  appId?: string;
  /** license 许可证（授权码） */
  license?: string;
  /** periodStart 有效期开始 */
  periodStart?: string;
  /** periodEnd 有效期结束 */
  periodEnd?: string;
  /** longTerm 是否长期有效 */
  longTerm?: boolean;
  /** valid 是否有效 */
  valid?: boolean;
};

/** 许可证搜索 */
export async function licenseSearch(
  request: Request<PageParam<LicenseSearch>>
): Promise<Response<PageData<LicenseData>>> {
  return http.post<
    Request<PageParam<LicenseSearch>>,
    Response<PageData<LicenseData>>
  >(`/api/license/search`, { data: request });
}

/** 许可证查询 */
export async function licenseSelect(
  request: Request<IdParam>
): Promise<Response<LicenseData>> {
  return http.post<Request<IdParam>, Response<LicenseData>>(
    `/api/license/select`,
    { data: request }
  );
}

/** 许可证写入 */
export async function licenseUpsert(
  request: Request<LicenseUpsert>
): Promise<Response<LicenseData>> {
  return http.post<Request<LicenseUpsert>, Response<LicenseData>>(
    `/api/license/upsert`,
    { data: request }
  );
}
