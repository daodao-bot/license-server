import { http } from "@/utils/http";
import type { IdParam, PageData, PageParam, Request, Response } from "@/api";

export type CustomerData = {
  /** id ID */
  id: string;
  /** name 客户名称 */
  name: string;
  /** phone 客户手机 */
  phone: string;
  /** name 客户邮箱 */
  email: string;
  /** createTime 创建时间 */
  createTime: string;
  /** updateTime 更新时间 */
  updateTime: string;
  /** valid 是否有效 */
  valid: boolean;
};

export type CustomerSearch = {
  /** name 客户名称 */
  name?: string;
  /** phone 客户手机 */
  phone?: string;
  /** name 客户邮箱 */
  email?: string;
  /** valid 是否有效 */
  valid?: boolean;
};

export type CustomerUpsert = {
  /** id ID */
  id?: string;
  /** name 客户名称 */
  name: string;
  /** phone 客户手机 */
  phone: string;
  /** name 客户邮箱 */
  email: string;
  /** valid 是否有效 */
  valid?: boolean;
};

/** 客户搜索 */
export async function customerSearch(
  request: Request<PageParam<CustomerSearch>>
): Promise<Response<PageData<CustomerData>>> {
  return http.post<
    Request<PageParam<CustomerSearch>>,
    Response<PageData<CustomerData>>
  >(`/api/customer/search`, { data: request });
}

/** 客户查询 */
export async function customerSelect(
  request: Request<IdParam>
): Promise<Response<CustomerData>> {
  return http.post<Request<IdParam>, Response<CustomerData>>(
    `/api/customer/select`,
    { data: request }
  );
}

/** 客户写入 */
export async function customerUpsert(
  request: Request<CustomerUpsert>
): Promise<Response<CustomerData>> {
  return http.post<Request<CustomerUpsert>, Response<CustomerData>>(
    `/api/customer/upsert`,
    { data: request }
  );
}
