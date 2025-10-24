import request from '@/utils/request'

// 查询商圈信息列表
export function listDistrict(query) {
  return request({
    url: '/system/district/list',
    method: 'get',
    params: query
  })
}

export function listDistrictout(query) {
  return request({
    url: '/system/district/listout',
    method: 'get',
    params: query
  })
}

// 查询商圈信息详细
export function getDistrict(id) {
  return request({
    url: '/system/district/' + id,
    method: 'get'
  })
}

// 新增商圈信息
export function addDistrict(data) {
  return request({
    url: '/system/district',
    method: 'post',
    data: data
  })
}

// 修改商圈信息
export function updateDistrict(data) {
  return request({
    url: '/system/district',
    method: 'put',
    data: data
  })
}

export function batchSetDistrictClient(data) {
  return request({
    url: '/system/district/batchSetClient',
    method: 'post',
    data: data
  })
}

// 删除商圈信息
export function delDistrict(id) {
  return request({
    url: '/system/district/' + id,
    method: 'delete'
  })
}
