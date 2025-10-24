import request from '@/utils/request'

// 查询邀请记录列表
export function listInvite(query) {
  return request({
    url: '/system/invite/list',
    method: 'get',
    params: query
  })
}

// 查询邀请记录详细
export function getInvite(id) {
  return request({
    url: '/system/invite/' + id,
    method: 'get'
  })
}

// 新增邀请记录
export function addInvite(data) {
  return request({
    url: '/system/invite',
    method: 'post',
    data: data
  })
}

// 修改邀请记录
export function updateInvite(data) {
  return request({
    url: '/system/invite',
    method: 'put',
    data: data
  })
}

// 删除邀请记录
export function delInvite(id) {
  return request({
    url: '/system/invite/' + id,
    method: 'delete'
  })
}

// 查询预约时间段
export function queryTimeByDate(query) {
  return request({
    url: '/system/invite/queryTimeByDate',
    method: 'get',
    params: query
  })
}