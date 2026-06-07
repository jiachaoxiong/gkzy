// 直接用mysql2驱动导入数据，避免shell编码问题
const mysql = require('mysql2/promise');
const fs = require('fs');

async function importData() {
  const conn = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '123456',
    charset: 'utf8mb4',
    multipleStatements: true
  });

  console.log('✅ MySQL连接成功');

  // 重建数据库
  await conn.query('DROP DATABASE IF EXISTS gkzy');
  await conn.query('CREATE DATABASE gkzy CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci');
  await conn.query('USE gkzy');
  console.log('✅ 数据库重建完成');

  // 读取并执行 schema.sql
  const schema = fs.readFileSync('d:/ccceshi/gkzy/gkzy-server/src/main/resources/db/schema.sql', 'utf-8');
  await conn.query(schema);
  console.log('✅ 表结构创建完成');

  // 读取并执行 data-v3.sql
  const data = fs.readFileSync('d:/ccceshi/gkzy/gkzy-server/src/main/resources/db/data-v3.sql', 'utf-8');
  // 按分号分割执行（简化的SQL解析）
  const statements = data.split(';\n').filter(s => s.trim() && !s.trim().startsWith('--')).map(s => s.trim() + ';');

  let count = 0;
  for (const stmt of statements) {
    if (stmt.startsWith('SET NAMES') || stmt.startsWith('--') || !stmt.trim()) continue;
    try {
      await conn.query(stmt);
      count++;
      if (count % 500 === 0) console.log(`   已执行 ${count} 条语句...`);
    } catch (err) {
      console.error(`❌ 失败: ${stmt.substring(0, 100)}...\n   ${err.message}`);
    }
  }
  console.log(`✅ 数据导入完成: ${count} 条`);

  // 验证
  const [rows] = await conn.query('SELECT id, name, province FROM college LIMIT 3');
  console.log('📋 验证数据:');
  rows.forEach(r => console.log(`   ${r.id} | ${r.name} | ${r.province}`));

  const [cnt] = await conn.query('SELECT COUNT(*) as cnt FROM college');
  console.log(`   总计: ${cnt[0].cnt} 所院校`);

  await conn.end();
  console.log('✅ 全部完成!');
}

importData().catch(err => { console.error('失败:', err.message); process.exit(1); });
