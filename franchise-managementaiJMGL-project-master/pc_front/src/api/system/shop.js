import request from '@/utils/request'

// 查询标准作业流程/店铺信息列表
export function listShop(query) {
  return request({
    url: '/system/shop/list',
    method: 'get',
    params: query
  })
}

// 查询标准作业流程/店铺信息详细
export function getShop(id) {
  return request({
    url: '/system/shop/' + id,
    method: 'get'
  })
}

// 新增标准作业流程/店铺信息
export function addShop(data) {
  return request({
    url: '/system/shop',
    method: 'post',
    data: data
  })
}

// 修改标准作业流程/店铺信息
export function updateShop(data) {
  return request({
    url: '/system/shop',
    method: 'put',
    data: data
  })
}

// 删除标准作业流程/店铺信息
export function delShop(id) {
  return request({
    url: '/system/shop/' + id,
    method: 'delete'
  })
}

export function updatenullShop(id) {
  return request({
    url: '/system/shop/removeFollower/' + id,
    method: 'delete'
  })
}

export function BatchSetProple(data) {
  // todo
  return request({
    url: '/system/district/addFollowers',
    method: 'post',
    data: data
  })
}

export function auditAcc(id) {
  return request({
    url: '/system/shop/auditAcc/' + id,
    method: 'delete'
  })
}

export function audit(data) {
  return request({
    url: '/system/district/audit',
    method: 'post',
    data: data
  })
}

export function toDesgin(id) {
  return request({
    url: '/system/shop/toDesgin/' + id,
    method: 'delete'
  })
}

