import request from '../utils/request';
import qs from 'qs';

// 公共请求


export async function query(params) {
  return request(`/api/example?${qs.stringify(params)}`);
}
