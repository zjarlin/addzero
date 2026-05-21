import sys
sys.stdout.write("SCRIPT START\n")
sys.stdout.flush()

import pandas as pd
import re
from datetime import datetime

src = "/Users/zjarlin/Downloads/附件1：沁伊3标房建项目施工台账(3)--2025.6.23日发.xlsx"
out = "/Users/zjarlin/Downloads/商混台账汇总结果.xlsx"

tpl_cols = ['项目名称', '制作日期', '楼号', '浇筑部位', '强度等级', '抗渗等级', '备注',
            '实测塌落度', '进场时间', '方量', '车号', '商混站名称', '混凝土浇筑方式', '混凝土振捣方式', '附加属性', '部位标签']

all_rows = []
xls = pd.ExcelFile(src)

for sn in xls.sheet_names:
    df_raw = pd.read_excel(src, sheet_name=sn, header=None)
    楼号 = sn.strip()
    ncols = df_raw.shape[1]

    if ncols == 15:
        idx_date = 2; idx_part = 3; idx_str = 4; idx_slump = 5; idx_vol = 6
        idx_note = 9; idx_station = 10; idx_grade = 11
        idx_contact = 12; idx_anti = 14
    else:
        idx_date = 1; idx_part = 2; idx_str = 3; idx_slump = 4; idx_vol = 5
        idx_note = 8; idx_station = 9; idx_grade = 10
        idx_contact = 11; idx_anti = 13

    for i in range(2, df_raw.shape[0]):
        row = df_raw.iloc[i]
        seq = row.iloc[0]
        try:
            int(float(seq))
        except (ValueError, TypeError):
            continue

        date_val = row.iloc[idx_date]
        part_val = row.iloc[idx_part]
        str_val = row.iloc[idx_str]
        slump_val = row.iloc[idx_slump]
        vol_val = row.iloc[idx_vol]
        note_val = row.iloc[idx_note]
        station_val = row.iloc[idx_station]
        contact_val = row.iloc[idx_contact]
        anti_val = row.iloc[idx_anti]

        part_s = str(part_val) if pd.notna(part_val) else ''
        if part_s in ('', 'nan') or '开工报告' in part_s or '场地整平' in part_s or '触探' in part_s:
            continue
        if pd.isna(date_val):
            continue

        str_s = str(str_val) if pd.notna(str_val) else ''
        grade = ''
        anti_grade = ''
        m = re.match(r'(C\d+)', str_s)
        if m:
            grade = m.group(1)
        m2 = re.search(r'(P\d+)', str_s)
        if m2:
            anti_grade = m2.group(1)

        station = ''
        if pd.notna(station_val):
            try:
                station = f'{int(float(station_val))}号拌合站'
            except:
                station = str(station_val)

        slump = ''
        if pd.notna(slump_val):
            try:
                slump = int(float(slump_val))
            except:
                slump = str(slump_val)

        vol = ''
        if pd.notna(vol_val):
            try:
                vol = float(vol_val)
            except:
                vol = str(vol_val)

        time_s = ''
        if pd.notna(date_val):
            if isinstance(date_val, datetime):
                time_s = date_val.strftime('%Y-%m-%d')
            else:
                time_s = str(date_val)

        note = str(note_val) if pd.notna(note_val) else ''
        if note == 'nan':
            note = ''

        contact = str(contact_val) if pd.notna(contact_val) else ''
        if contact and contact != 'nan':
            note = f"{note} {contact}".strip() if note else contact

        all_rows.append({
            '项目名称': '沁伊3标房建项目',
            '制作日期': time_s,
            '楼号': 楼号,
            '浇筑部位': part_s,
            '强度等级': grade,
            '抗渗等级': anti_grade,
            '备注': note,
            '实测塌落度': slump,
            '进场时间': time_s,
            '方量': vol,
            '车号': '',
            '商混站名称': station,
            '混凝土浇筑方式': '',
            '混凝土振捣方式': '',
            '附加属性': '',
            '部位标签': ''
        })

sys.stdout.write(f"Total rows: {len(all_rows)}\n")
sys.stdout.flush()

result_df = pd.DataFrame(all_rows, columns=tpl_cols)
result_df.to_excel(out, index=False, sheet_name='商混台账', engine='openpyxl')

sys.stdout.write(f"DONE: {out}\n")
sys.stdout.flush()
