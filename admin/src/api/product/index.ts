import { http } from "@/utils/http";
import type { IdParam, PageData, PageParam, Request, Response } from "@/api";

export type ProductData = {
  /** id ID */
  id: string;
  /** code 产品代码：支持最长 32 位字符串，可以包含数字、大小写字母、- 或 _ */
  code: string;
  /** name 产品名称 */
  name: string;
  /** createTime 创建时间 */
  createTime: string;
  /** updateTime 更新时间 */
  updateTime: string;
  /** valid 是否有效 */
  valid: boolean;
};

export type ProductSearch = {
  /** code 产品代码：支持最长 32 位字符串，可以包含数字、大小写字母、- 或 _ */
  code?: string;
  /** name 产品名称 */
  name?: string;
  /** valid 是否有效 */
  valid?: boolean;
};

export type ProductUpsert = {
  /** id ID */
  id?: string;
  /** code 产品代码：支持最长 32 位字符串，可以包含数字、大小写字母、- 或 _ */
  code: string;
  /** name 产品名称：如果不传值，默认使用 code 的值 */
  name?: string;
  /** valid 是否有效 */
  valid?: boolean;
};

/** 产品搜索 */
export async function productSearch(
  request: Request<PageParam<ProductSearch>>
): Promise<Response<PageData<ProductData>>> {
  return http.post<
    Request<PageParam<ProductSearch>>,
    Response<PageData<ProductData>>
  >(`/api/product/search`, { data: request });
}

/** 产品查询 */
export async function productSelect(
  request: Request<IdParam>
): Promise<Response<ProductData>> {
  return http.post<Request<IdParam>, Response<ProductData>>(
    `/api/product/select`,
    { data: request }
  );
}

/** 产品写入 */
export async function productUpsert(
  request: Request<ProductUpsert>
): Promise<Response<ProductData>> {
  return http.post<Request<ProductUpsert>, Response<ProductData>>(
    `/api/product/upsert`,
    { data: request }
  );
}
