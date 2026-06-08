<template>
  <router-view />
</template>

<style>
/* 全局移动端适配 */
@media (max-width: 768px) {
  .el-table {
    font-size: 13px;
    overflow-x: auto;
    display: block;
  }
  .el-card {
    border-radius: 8px;
  }
  .el-card__header {
    padding: 12px 16px;
  }
  .el-card__body {
    padding: 12px 16px;
  }
  .el-pagination {
    justify-content: center;
  }
  .el-pagination button,
  .el-pagination .el-pager li {
    min-width: 28px;
    height: 28px;
    line-height: 28px;
    font-size: 12px;
  }
  .el-dialog {
    width: 92% !important;
    margin-top: 10vh !important;
  }
  .el-form-item__label {
    font-size: 13px;
  }
  .el-descriptions__body .el-descriptions__table {
    table-layout: fixed;
  }
}
</style>
