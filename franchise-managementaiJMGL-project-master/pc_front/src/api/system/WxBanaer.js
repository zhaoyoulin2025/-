import request from '@/utils/request'

// 查询wxbanner列表
export function listBanner(query) {
  return request({
    url: '/system/tbWxBanner/list',
    method: 'get',
    params: query
  })
}

// 查询wxbanner详细
export function getBanner(id) {
  return request({
    url: '/system/tbWxBanner/detail/' + id,
    method: 'get'
  })
}

// 新增wxbanner
export function addBanner(data) {
  return request({
    url: '/system/tbWxBanner/addOrUpdate',
    method: 'post',
    data: data
  })
}

// 修改wxbanner
export function updateBanner(data) {
  return request({
    url: '/system/tbWxBanner/addOrUpdate',
    method: 'post',
    data: data
  })
}

// 删除wxbanner
export function delBanner(id) {
  return request({
    url: '/system/tbWxBanner/' + id,
    method: 'delete'
  })
}
