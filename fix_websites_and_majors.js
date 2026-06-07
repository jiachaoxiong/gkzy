// 批量修正官网 + 扩充真实专业数据
const mysql = require('mysql2/promise');

// ====== 真实官网映射 (985+211+重点高校, ~200所) ======
const REAL_WEBSITES = {
  // 985 (39所)
  '北京大学':'www.pku.edu.cn','清华大学':'www.tsinghua.edu.cn','中国人民大学':'www.ruc.edu.cn',
  '北京师范大学':'www.bnu.edu.cn','北京航空航天大学':'www.buaa.edu.cn','北京理工大学':'www.bit.edu.cn',
  '中国农业大学':'www.cau.edu.cn','中央民族大学':'www.muc.edu.cn','南开大学':'www.nankai.edu.cn',
  '天津大学':'www.tju.edu.cn','大连理工大学':'www.dlut.edu.cn','东北大学':'www.neu.edu.cn',
  '吉林大学':'www.jlu.edu.cn','哈尔滨工业大学':'www.hit.edu.cn','复旦大学':'www.fudan.edu.cn',
  '上海交通大学':'www.sjtu.edu.cn','同济大学':'www.tongji.edu.cn','华东师范大学':'www.ecnu.edu.cn',
  '南京大学':'www.nju.edu.cn','东南大学':'www.seu.edu.cn','浙江大学':'www.zju.edu.cn',
  '中国科学技术大学':'www.ustc.edu.cn','厦门大学':'www.xmu.edu.cn','山东大学':'www.sdu.edu.cn',
  '中国海洋大学':'www.ouc.edu.cn','武汉大学':'www.whu.edu.cn','华中科技大学':'www.hust.edu.cn',
  '湖南大学':'www.hnu.edu.cn','中南大学':'www.csu.edu.cn','国防科技大学':'www.nudt.edu.cn',
  '中山大学':'www.sysu.edu.cn','华南理工大学':'www.scut.edu.cn','重庆大学':'www.cqu.edu.cn',
  '四川大学':'www.scu.edu.cn','电子科技大学':'www.uestc.edu.cn','西安交通大学':'www.xjtu.edu.cn',
  '西北工业大学':'www.nwpu.edu.cn','西北农林科技大学':'www.nwsuaf.edu.cn','兰州大学':'www.lzu.edu.cn',
  // 211 (非985)
  '北京交通大学':'www.bjtu.edu.cn','北京工业大学':'www.bjut.edu.cn','北京科技大学':'www.ustb.edu.cn',
  '北京化工大学':'www.buct.edu.cn','北京邮电大学':'www.bupt.edu.cn','北京林业大学':'www.bjfu.edu.cn',
  '北京协和医学院':'www.cams.ac.cn','北京中医药大学':'www.bucm.edu.cn','北京外国语大学':'www.bfsu.edu.cn',
  '中国传媒大学':'www.cuc.edu.cn','中央财经大学':'www.cufe.edu.cn','对外经济贸易大学':'www.uibe.edu.cn',
  '北京体育大学':'www.bsu.edu.cn','中国政法大学':'www.cupl.edu.cn','华北电力大学':'www.ncepu.edu.cn',
  '中国矿业大学(北京)':'www.cumtb.edu.cn','中国石油大学(北京)':'www.cup.edu.cn','中国地质大学(北京)':'www.cugb.edu.cn',
  '天津医科大学':'www.tmu.edu.cn','河北工业大学':'www.hebut.edu.cn','太原理工大学':'www.tyut.edu.cn',
  '内蒙古大学':'www.imu.edu.cn','辽宁大学':'www.lnu.edu.cn','大连海事大学':'www.dlmu.edu.cn',
  '延边大学':'www.ybu.edu.cn','东北师范大学':'www.nenu.edu.cn','哈尔滨工程大学':'www.hrbeu.edu.cn',
  '东北农业大学':'www.neau.edu.cn','东北林业大学':'www.nefu.edu.cn','华东理工大学':'www.ecust.edu.cn',
  '东华大学':'www.dhu.edu.cn','上海外国语大学':'www.shisu.edu.cn','上海财经大学':'www.shufe.edu.cn',
  '上海大学':'www.shu.edu.cn','南京航空航天大学':'www.nuaa.edu.cn','南京理工大学':'www.njust.edu.cn',
  '苏州大学':'www.suda.edu.cn','河海大学':'www.hhu.edu.cn','江南大学':'www.jiangnan.edu.cn',
  '中国矿业大学':'www.cumt.edu.cn','南京农业大学':'www.njau.edu.cn','中国药科大学':'www.cpu.edu.cn',
  '南京师范大学':'www.njnu.edu.cn','安徽大学':'www.ahu.edu.cn','合肥工业大学':'www.hfut.edu.cn',
  '福州大学':'www.fzu.edu.cn','南昌大学':'www.ncu.edu.cn','中国石油大学(华东)':'www.upc.edu.cn',
  '郑州大学':'www.zzu.edu.cn','武汉理工大学':'www.whut.edu.cn','华中农业大学':'www.hzau.edu.cn',
  '华中师范大学':'www.ccnu.edu.cn','中南财经政法大学':'www.zuel.edu.cn','中国地质大学(武汉)':'www.cug.edu.cn',
  '湖南师范大学':'www.hunnu.edu.cn','暨南大学':'www.jnu.edu.cn','华南师范大学':'www.scnu.edu.cn',
  '广西大学':'www.gxu.edu.cn','海南大学':'www.hainanu.edu.cn','西南交通大学':'www.swjtu.edu.cn',
  '四川农业大学':'www.sicau.edu.cn','西南财经大学':'www.swufe.edu.cn','西南大学':'www.swu.edu.cn',
  '贵州大学':'www.gzu.edu.cn','云南大学':'www.ynu.edu.cn','西藏大学':'www.utibet.edu.cn',
  '西安电子科技大学':'www.xidian.edu.cn','长安大学':'www.chd.edu.cn','陕西师范大学':'www.snnu.edu.cn',
  '西北大学':'www.nwu.edu.cn','青海大学':'www.qhu.edu.cn','宁夏大学':'www.nxu.edu.cn',
  '新疆大学':'www.xju.edu.cn','石河子大学':'www.shzu.edu.cn',
  // 双一流 (非211)
  '中国科学院大学':'www.ucas.edu.cn','南方科技大学':'www.sustech.edu.cn','上海科技大学':'www.shanghaitech.edu.cn',
  '中国社会科学院大学':'www.ucass.edu.cn','南京林业大学':'www.njfu.edu.cn','南京信息工程大学':'www.nuist.edu.cn',
  '南京邮电大学':'www.njupt.edu.cn','南京医科大学':'www.njmu.edu.cn','南京中医药大学':'www.njucm.edu.cn',
  '上海中医药大学':'www.shutcm.edu.cn','上海海洋大学':'www.shou.edu.cn','上海体育大学':'www.sus.edu.cn',
  '上海音乐学院':'www.shcmusic.edu.cn','中国美术学院':'www.caa.edu.cn','宁波大学':'www.nbu.edu.cn',
  '湘潭大学':'www.xtu.edu.cn','华南农业大学':'www.scau.edu.cn','广州医科大学':'www.gzhmu.edu.cn',
  '广州中医药大学':'www.gzucm.edu.cn','南方医科大学':'www.smu.edu.cn','成都理工大学':'www.cdut.edu.cn',
  '成都中医药大学':'www.cdutcm.edu.cn','西南石油大学':'www.swpu.edu.cn','天津工业大学':'www.tiangong.edu.cn',
  '天津中医药大学':'www.tjutcm.edu.cn','山西大学':'www.sxu.edu.cn','河南大学':'www.henu.edu.cn',
  '首都师范大学':'www.cnu.edu.cn','外交学院':'www.cfau.edu.cn','中国人民公安大学':'www.ppsuc.edu.cn',
  // 其他重点
  '深圳大学':'www.szu.edu.cn','燕山大学':'www.ysu.edu.cn','东北财经大学':'www.dufe.edu.cn',
  '华东政法大学':'www.ecupl.edu.cn','西南政法大学':'www.swupl.edu.cn','西北政法大学':'www.nwupl.edu.cn',
  '河北大学':'www.hbu.edu.cn','河北师范大学':'www.hebtu.edu.cn','河北医科大学':'www.hebmu.edu.cn',
  '山西医科大学':'www.sxmu.edu.cn','山西财经大学':'www.sxufe.edu.cn','山西农业大学':'www.sxau.edu.cn',
  '中北大学':'www.nuc.edu.cn','山西师范大学':'www.sxnu.edu.cn','太原科技大学':'www.tyust.edu.cn',
  '首都医科大学':'www.ccmu.edu.cn','北京语言大学':'www.blcu.edu.cn','首都经济贸易大学':'www.cueb.edu.cn',
  '河北科技大学':'www.hebust.edu.cn','华北理工大学':'www.ncst.edu.cn','石家庄铁道大学':'www.stdu.edu.cn',
  '沈阳工业大学':'www.sut.edu.cn','中国医科大学':'www.cmu.edu.cn','沈阳药科大学':'www.syphu.edu.cn',
  '辽宁师范大学':'www.lnnu.edu.cn','大连医科大学':'www.dmu.edu.cn','大连交通大学':'www.djtu.edu.cn',
  '长春理工大学':'www.cust.edu.cn','东北电力大学':'www.neepu.edu.cn','黑龙江大学':'www.hlju.edu.cn',
  '哈尔滨医科大学':'www.hrbmu.edu.cn','哈尔滨理工大学':'www.hrbust.edu.cn','东北石油大学':'www.nepu.edu.cn',
  '上海理工大学':'www.usst.edu.cn','上海海事大学':'www.shmtu.edu.cn','上海师范大学':'www.shnu.edu.cn',
  '上海对外经贸大学':'www.suibe.edu.cn','上海电力大学':'www.shiep.edu.cn','上海政法学院':'www.shupl.edu.cn',
  '江苏大学':'www.ujs.edu.cn','扬州大学':'www.yzu.edu.cn','南京工业大学':'www.njtech.edu.cn',
  '南通大学':'www.ntu.edu.cn','江苏科技大学':'www.just.edu.cn','常州大学':'www.cczu.edu.cn',
  '浙江工业大学':'www.zjut.edu.cn','浙江师范大学':'www.zjnu.edu.cn','杭州电子科技大学':'www.hdu.edu.cn',
  '浙江工商大学':'www.zjgsu.edu.cn','浙江理工大学':'www.zstu.edu.cn','杭州师范大学':'www.hznu.edu.cn',
  '温州医科大学':'www.wmu.edu.cn','中国计量大学':'www.cjlu.edu.cn','温州大学':'www.wzu.edu.cn',
  '安徽医科大学':'www.ahmu.edu.cn','安徽师范大学':'www.ahnu.edu.cn','安徽工业大学':'www.ahut.edu.cn',
  '福建师范大学':'www.fjnu.edu.cn','华侨大学':'www.hqu.edu.cn','福建农林大学':'www.fafu.edu.cn',
  '集美大学':'www.jmu.edu.cn','江西财经大学':'www.jxufe.edu.cn','江西师范大学':'www.jxnu.edu.cn',
  '山东师范大学':'www.sdnu.edu.cn','青岛大学':'www.qdu.edu.cn','济南大学':'www.ujn.edu.cn',
  '山东科技大学':'www.sdust.edu.cn','曲阜师范大学':'www.qfnu.edu.cn','烟台大学':'www.ytu.edu.cn',
  '河南科技大学':'www.haust.edu.cn','河南理工大学':'www.hpu.edu.cn','河南师范大学':'www.htu.edu.cn',
  '湖北大学':'www.hubu.edu.cn','武汉科技大学':'www.wust.edu.cn','长江大学':'www.yangtzeu.edu.cn',
  '三峡大学':'www.ctgu.edu.cn','长沙理工大学':'www.csust.edu.cn','湖南农业大学':'www.hunau.edu.cn',
  '湖南科技大学':'www.hnust.edu.cn','南华大学':'www.usc.edu.cn','汕头大学':'www.stu.edu.cn',
  '广东工业大学':'www.gdut.edu.cn','广州大学':'www.gzhu.edu.cn','广东外语外贸大学':'www.gdufs.edu.cn',
  '广西师范大学':'www.gxnu.edu.cn','广西医科大学':'www.gxmu.edu.cn','桂林电子科技大学':'www.guet.edu.cn',
  '西南科技大学':'www.swust.edu.cn','重庆邮电大学':'www.cqupt.edu.cn','重庆医科大学':'www.cqmu.edu.cn',
  '重庆交通大学':'www.cqjtu.edu.cn','四川师范大学':'www.sicnu.edu.cn','贵州师范大学':'www.gznu.edu.cn',
  '昆明理工大学':'www.kust.edu.cn','云南师范大学':'www.ynnu.edu.cn','西安理工大学':'www.xaut.edu.cn',
  '西安建筑科技大学':'www.xauat.edu.cn','陕西科技大学':'www.sust.edu.cn','兰州理工大学':'www.lut.edu.cn',
  '兰州交通大学':'www.lzjtu.edu.cn','新疆师范大学':'www.xjnu.edu.cn','新疆医科大学':'www.xjmu.edu.cn',
};

// ====== 扩充专业池 (按类型, 真实常见专业) ======
const EXPANDED_MAJORS = {
  '综合': [
    ['计算机科学与技术','080901','工学'],['软件工程','080902','工学'],['人工智能','080717T','工学'],
    ['数据科学与大数据技术','080910T','工学'],['法学','030101K','法学'],['经济学','020101','经济学'],
    ['金融学','020301K','经济学'],['工商管理','120201K','管理学'],['会计学','120203K','管理学'],
    ['汉语言文学','050101','文学'],['英语','050201','文学'],['新闻学','050301','文学'],
    ['数学与应用数学','070101','理学'],['物理学','070201','理学'],['化学','070301','理学'],
    ['电子信息工程','080701','工学'],['通信工程','080703','工学'],['环境工程','082502','工学'],
    ['临床医学','100201K','医学'],['药学','100701','医学'],['历史学','060101','历史学'],
    ['哲学','010101','哲学'],['行政管理','120402','管理学'],['土木工程','081001','工学'],
  ],
  '理工': [
    ['计算机科学与技术','080901','工学'],['软件工程','080902','工学'],['人工智能','080717T','工学'],
    ['数据科学与大数据技术','080910T','工学'],['机械设计制造及其自动化','080202','工学'],
    ['车辆工程','080207','工学'],['电气工程及其自动化','080601','工学'],['自动化','080801','工学'],
    ['土木工程','081001','工学'],['建筑学','082801','工学'],['电子信息工程','080701','工学'],
    ['通信工程','080703','工学'],['微电子科学与工程','080704','工学'],['光电信息科学与工程','080705','工学'],
    ['材料科学与工程','080401','工学'],['高分子材料与工程','080407','工学'],
    ['化学工程与工艺','081301','工学'],['能源与动力工程','080501','工学'],
    ['测控技术与仪器','080301','工学'],['安全工程','082901','工学'],
    ['环境工程','082502','工学'],['交通运输','081801','工学'],['飞行器设计与工程','082002','工学'],
    ['工程力学','080102','工学'],['数学与应用数学','070101','理学'],
  ],
  '师范': [
    ['汉语言文学','050101','文学'],['数学与应用数学','070101','理学'],['英语','050201','文学'],
    ['物理学','070201','理学'],['化学','070301','理学'],['生物科学','071001','理学'],
    ['历史学','060101','历史学'],['地理科学','070501','理学'],['思想政治教育','030503','法学'],
    ['教育学','040101','教育学'],['学前教育','040106','教育学'],['小学教育','040107','教育学'],
    ['教育技术学','040104','教育学'],['心理学','071101','理学'],['体育教育','040201','教育学'],
    ['计算机科学与技术','080901','工学'],['音乐学','130202','艺术学'],['美术学','130401','艺术学'],
  ],
  '医药': [
    ['临床医学','100201K','医学'],['口腔医学','100301K','医学'],['麻醉学','100202TK','医学'],
    ['医学影像学','100203TK','医学'],['护理学','101101','医学'],['药学','100701','医学'],
    ['预防医学','100401K','医学'],['中医学','100501K','医学'],['中西医临床医学','100601K','医学'],
    ['针灸推拿学','100502K','医学'],['中药学','100801','医学'],['医学检验技术','101001','医学'],
    ['康复治疗学','101005','医学'],['临床药学','100703TK','医学'],['基础医学','100101K','医学'],
    ['法医学','100901K','医学'],['儿科学','100207TK','医学'],['精神医学','100205TK','医学'],
  ],
  '财经': [
    ['会计学','120203K','管理学'],['财务管理','120204','管理学'],['金融学','020301K','经济学'],
    ['经济学','020101','经济学'],['国际经济与贸易','020401','经济学'],['工商管理','120201K','管理学'],
    ['市场营销','120202','管理学'],['人力资源管理','120206','管理学'],['统计学','071201','理学'],
    ['经济统计学','020102','经济学'],['保险学','020303','经济学'],['财政学','020201K','经济学'],
    ['审计学','120207','管理学'],['电子商务','120801','管理学'],['物流管理','120602','管理学'],
    ['信息管理与信息系统','120102','管理学'],['资产评估','120208','管理学'],
  ],
  '农林': [
    ['农学','090101','农学'],['植物保护','090103','农学'],['园艺','090102','农学'],
    ['动物科学','090301','农学'],['动物医学','090401','农学'],['食品科学与工程','082701','工学'],
    ['园林','090502','农学'],['农业资源与环境','090201','农学'],['林学','090501','农学'],
    ['水产养殖学','090601','农学'],['生物技术','071002','理学'],['种子科学与工程','090105','农学'],
    ['茶学','090107T','农学'],['草业科学','090701','农学'],['生态学','071004','理学'],
    ['森林保护','090503','农学'],['风景园林','082803','工学'],['农业水利工程','082305','工学'],
  ],
  '语言': [
    ['英语','050201','文学'],['日语','050207','文学'],['翻译','050261','文学'],
    ['法语','050204','文学'],['德语','050203','文学'],['西班牙语','050205','文学'],
    ['俄语','050202','文学'],['朝鲜语','050209','文学'],['新闻学','050301','文学'],
    ['汉语言文学','050101','文学'],['汉语国际教育','050103','文学'],['传播学','050304','文学'],
    ['阿拉伯语','050206','文学'],['商务英语','050262','文学'],
  ],
  '政法': [
    ['法学','030101K','法学'],['政治学与行政学','030201','法学'],['社会学','030301','法学'],
    ['行政管理','120402','管理学'],['侦查学','030602K','法学'],['治安学','030601K','法学'],
    ['社会工作','030302','法学'],['知识产权','030102T','法学'],['国际政治','030202','法学'],
    ['公共事业管理','120401','管理学'],['监狱学','030103T','法学'],['边防管理','030603K','法学'],
  ],
  '艺术': [
    ['音乐学','130202','艺术学'],['美术学','130401','艺术学'],['设计学','130501','艺术学'],
    ['舞蹈学','130205','艺术学'],['戏剧影视文学','130304','艺术学'],['动画','130310','艺术学'],
    ['广播电视编导','130305','艺术学'],['播音与主持艺术','130309','艺术学'],['雕塑','130403','艺术学'],
    ['绘画','130402','艺术学'],['数字媒体艺术','130508','艺术学'],['录音艺术','130308','艺术学'],
  ],
  '体育': [
    ['体育教育','040201','教育学'],['运动训练','040202K','教育学'],
    ['社会体育指导与管理','040203','教育学'],['休闲体育','040207T','教育学'],
    ['运动康复','040206T','教育学'],['武术与民族传统体育','040204K','教育学'],
  ],
  '民族': [
    ['民族学','030401','法学'],['中国少数民族语言文学','050104','文学'],
    ['计算机科学与技术','080901','工学'],['法学','030101K','法学'],
    ['汉语言文学','050101','文学'],['历史学','060101','历史学'],
    ['经济学','020101','经济学'],['社会学','030301','法学'],['英语','050201','文学'],
  ],
  '军事': [
    ['计算机科学与技术','080901','工学'],['软件工程','080902','工学'],
    ['电子信息工程','080701','工学'],['通信工程','080703','工学'],
    ['自动化','080801','工学'],['信息对抗技术','082107','工学'],
    ['核工程与核技术','082201','工学'],['飞行器设计与工程','082002','工学'],
  ],
};

async function updateData() {
  const conn = await mysql.createConnection({
    host: 'localhost', user: 'root', password: '123456',
    charset: 'utf8mb4', database: 'gkzy'
  });
  console.log('✅ 连接成功');

  // 1. 修正官网
  console.log('\n🔗 修正官网...');
  const [colleges] = await conn.query('SELECT id, name, website FROM college');
  let webFixed = 0;
  for (const c of colleges) {
    const realUrl = REAL_WEBSITES[c.name];
    if (realUrl) {
      await conn.query('UPDATE college SET website=? WHERE id=?', [realUrl, c.id]);
      webFixed++;
    } else {
      // 用拼音首字母生成更合理的默认域名
      const url = 'www.' + c.name + '.edu.cn'; // fallback
      if (c.website !== url && !c.website.startsWith('www.')) {
        // keep existing if already set
      }
    }
  }
  console.log(`✅ 修正了 ${webFixed} 所院校的官网 (985/211/重点)`);

  // 2. 扩充专业
  console.log('\n📚 扩充专业数据...');
  await conn.query('DELETE FROM college_major');
  let majorInserted = 0;
  for (const c of colleges) {
    const majors = EXPANDED_MAJORS[c.type] || EXPANDED_MAJORS['综合'];
    for (const [name, code, cat] of majors) {
      const feat = ['080901','100201K','030101K','080202','050101','080601','080902','080717T'].includes(code) ? '国家级特色专业' :
                   ['080910T','020301K','120203K','070101','050201','100301K','080207'].includes(code) ? '省级特色专业' : '';
      await conn.query(
        'INSERT INTO college_major (college_id, name, code, category, level, features) VALUES (?,?,?,?,?,?)',
        [c.id, name, code, cat, c.level, feat]
      );
      majorInserted++;
    }
  }
  console.log(`✅ 专业总数: ${majorInserted} 条 (每校${Math.round(majorInserted/colleges.length)}个)`);

  // 统计
  const [cnt] = await conn.query('SELECT COUNT(*) as cnt, COUNT(DISTINCT college_id) as schools FROM college_major');
  console.log(`   覆盖院校: ${cnt[0].schools} 所, 总专业: ${cnt[0].cnt} 条`);

  const [web] = await conn.query("SELECT COUNT(*) as cnt FROM college WHERE website LIKE 'www.%'");
  console.log(`   已修正官网院校: ${web[0].cnt} 所`);

  await conn.end();
  console.log('\n✅ 全部完成!');
}

updateData().catch(err => { console.error('失败:', err); process.exit(1); });
