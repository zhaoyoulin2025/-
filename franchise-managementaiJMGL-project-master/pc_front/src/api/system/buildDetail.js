import request from '@/utils/request'

// 查询建设详情列表
export function listBuildDetail(query) {
  return request({
    url: '/system/buildDetail/list',
    method: 'get',
    params: query
  })
}

// 查询建设详情详细
export function getBuildDetail(id) {
  return request({
    url: '/system/buildDetail/' + id,
    method: 'get'
  })
}

// 新增建设详情
export function addBuildDetail(data) {
  return request({
    url: '/system/buildDetail',
    method: 'post',
    data: data
  })
}

// 修改建设详情
export function updateBuildDetail(data) {
  return request({
    url: '/system/buildDetail',
    method: 'put',
    data: data
  })
}

// 删除建设详情
export function delBuildDetail(id) {
  return request({
    url: '/system/buildDetail/' + id,
    method: 'delete'
  })
}
