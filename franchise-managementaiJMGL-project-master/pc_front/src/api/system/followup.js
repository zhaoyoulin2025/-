import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listFollowup(query) {
  return request({
    url: '/system/followup/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getFollowup(id) {
  return request({
    url: '/system/followup/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addFollowup(data) {
  return request({
    url: '/system/followup',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateFollowup(data) {
  return request({
    url: '/system/followup',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delFollowup(id) {
  return request({
    url: '/system/followup/' + id,
    method: 'delete'
  })
}
