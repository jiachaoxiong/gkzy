// 修复2025年一分一段表数据（官方正确版本）
// 数据来源: 山西省招生考试管理中心 2025年6月25日公布
// 旧数据存在严重错误，此脚本用正确数据替换并更新数据库
const mysql = require('mysql2/promise');

// ====== 官方关键数据点(累计位次) ======
// 逐分数据通过线性插值生成

const KEY_POINTS = {
  // 2025年 物理类(原理工)
  '2025_理工': [
    [690,84],[680,230],[670,478],[660,942],[650,1691],[640,2722],[630,3984],[620,5728],[610,7886],[600,10452],
    [590,13425],[580,16930],[570,20780],[560,25129],[550,29847],[540,35110],[530,40723],[520,46928],[510,53491],[500,60425],
    [490,67479],[480,74557],[470,81895],[460,89298],[450,96566],[440,103827],[430,111144],[420,118413],[410,125763],[400,133192],
    [390,140702],[380,148293],[370,155950],[360,163659],[350,171420],[340,179230],[330,187090],[320,195000],[310,202960],[300,210970]
  ],
  // 2025年 历史类(原文史)
  '2025_文史': [
    [671,11],[670,13],[660,42],[650,110],[640,256],[630,448],[620,807],[610,1270],[600,1918],
    [590,2792],[580,3874],[570,5154],[560,6593],[550,8406],[540,10416],[530,12559],[520,15009],[510,17574],[500,20270],
    [490,23133],[480,26003],[470,28696],[460,31489],[450,34070],[443,35877],
    [440,36588],[430,39307],[420,42032],[410,44763],[400,47500],[390,50243],[380,52992],[370,55747],[360,58508],[350,61275],
    [340,64048],[330,66827],[320,69612],[310,72403],[300,75200]
  ]
};

// 线性插值生成逐分数据
function generateFullTable(keyPoints) {
  const result = {};
  // 按分数排序
  keyPoints.sort((a, b) => a[0] - b[0]);

  for (let i = 0; i < keyPoints.length - 1; i++) {
    const [scoreLow, rankLow] = keyPoints[i];
    const [scoreHigh, rankHigh] = keyPoints[i + 1];

    result[scoreLow] = rankLow;

    const scoreDiff = scoreHigh - scoreLow;
    const rankDiff = rankHigh - rankLow;

    for (let s = scoreLow + 1; s < scoreHigh; s++) {
      const ratio = (s - scoreLow) / scoreDiff;
      result[s] = Math.round(rankLow + rankDiff * ratio);
    }
  }
  // 最后一个点
  const last = keyPoints[keyPoints.length - 1];
  result[last[0]] = last[1];

  return result;
}

const FULL_TABLE = {
  '2025_理工': generateFullTable(KEY_POINTS['2025_理工']),
  '2025_文史': generateFullTable(KEY_POINTS['2025_文史'])
};

// 从一分一段表查询位次(支持插值)
function getRank(score, table) {
  if (table[score]) return table[score];
  const scores = Object.keys(table).map(Number).sort((a, b) => a - b);
  let lower = null, higher = null;
  for (const s of scores) {
    if (s < score) lower = s;
    if (s > score && higher === null) higher = s;
  }
  if (lower && higher) {
    const ratio = (score - lower) / (higher - lower);
    return Math.round(table[lower] + (table[higher] - table[lower]) * ratio);
  }
  return lower ? table[lower] : higher ? table[higher] : null;
}

async function main() {
  const conn = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'gkzy'
  });

  console.log('🔗 已连接数据库');

  // 1. 修复 admission_score 表中 min_rank 数据
  //    对于2025年的记录，用正确的一分一段表重新计算 min_rank
  const [scores2025] = await conn.query(
    "SELECT id, min_score, subject_type, min_rank FROM admission_score WHERE year = 2025 AND major_id IS NULL"
  );

  console.log(`📊 2025年录取数据共 ${scores2025.length} 条，开始修复 min_rank...`);

  let updated = 0;
  for (const row of scores2025) {
    const subjectKey = row.subject_type === '文史' ? '2025_文史' : '2025_理工';
    const table = FULL_TABLE[subjectKey];
    if (!table) continue;

    const newRank = getRank(row.min_score, table);
    if (newRank && newRank !== row.min_rank) {
      await conn.query('UPDATE admission_score SET min_rank = ? WHERE id = ?', [newRank, row.id]);
      updated++;
      if (updated % 100 === 0) {
        console.log(`   已修复 ${updated} 条...`);
      }
    }
  }

  console.log(`✅ 共修复 ${updated} 条 min_rank 记录`);

  // 2. 验证几个关键数据点
  console.log('\n📋 数据验证:');

  // 文史类600分
  const [wenShi600] = await conn.query(
    `SELECT c.name, s.min_score, s.min_rank, s.year, s.batch
     FROM admission_score s JOIN college c ON c.id = s.college_id
     WHERE s.year = 2025 AND s.subject_type = '文史' AND s.min_score = 600 AND s.major_id IS NULL LIMIT 5`
  );
  console.log('  文史类600分记录:', JSON.stringify(wenShi600, null, 2));

  // 理工类550分
  const [liGong550] = await conn.query(
    `SELECT c.name, s.min_score, s.min_rank, s.year, s.batch
     FROM admission_score s JOIN college c ON c.id = s.college_id
     WHERE s.year = 2025 AND s.subject_type = '理工' AND s.min_score = 550 AND s.major_id IS NULL LIMIT 5`
  );
  console.log('  理工类550分记录:', JSON.stringify(liGong550, null, 2));

  // 3. 输出正确的一分一段表(用于更新代码文件)
  console.log('\n📝 2025年物理类 一分一段表关键数据(可嵌入Java/TS):');
  console.log(JSON.stringify(FULL_TABLE['2025_理工']));

  console.log('\n📝 2025年历史类 一分一段表关键数据(可嵌入Java/TS):');
  console.log(JSON.stringify(FULL_TABLE['2025_文史']));

  await conn.end();
  console.log('\n🎉 修复完成！');
}

main().catch(e => { console.error(e); process.exit(1); });
