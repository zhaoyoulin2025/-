import request from '@/utils/request'

// 查询文件存储列表
export function listFile(query) {
  return request({
    url: '/system/file/list',
    method: 'get',
    params: query
  })
}

// 查询文件存储详细
export function getFile(id) {
  return request({
    url: '/system/file/' + id,
    method: 'get'
  })
}

// 新增文件存储
export function addFile(data) {
  return request({
    url: '/system/file',
    method: 'post',
    data: data
  })
}

// 修改文件存储
export function updateFile(data) {
  return request({
    url: '/system/file',
    method: 'put',
    data: data
  })
}

// 删除文件存储
export function delFile(id) {
  return request({
    url: '/system/file/' + id,
    method: 'delete'
  })
}
