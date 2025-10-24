import request from '@/utils/request'

// 查询建设管理列表
export function shoplistBuild(query) {
  return request({
    url: '/system/build/shoplist',
    method: 'get',
    params: query
  })
}


export function listBuild(query) {
  return request({
    url: '/system/build/list',
    method: 'get',
    params: query
  })
}




export function addSuperviser(data) {
  return request({
    url: '/system/build/addSuperviser',
    method: 'post',
    data: data
  })
}

export function addFollower(data) {
  return request({
    url: '/system/build/addFollower',
    method: 'post',
    data: data
  })
}

// 查询建设管理详细
export function getBuild(id) {
  return request({
    url: '/system/build/' + id,
    method: 'get'
  })
}

// 新增建设管理
export function addBuild(data) {
  return request({
    url: '/system/build',
    method: 'post',
    data: data
  })
}

export function toBuild(id) {
  return request({
    url: '/system/build/toBuild/' + id,
    method: 'delete'
  })
}

// 修改建设管理
export function updateBuild(data) {
  return request({
    url: '/system/build',
    method: 'put',
    data: data
  })
}

// 删除建设管理
export function delBuild(id) {
  return request({
    url: '/system/build/' + id,
    method: 'delete'
  })
}
