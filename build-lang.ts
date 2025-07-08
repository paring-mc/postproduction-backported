import * as path from "jsr:@std/path";

const data051: Record<string, string> = await fetch('https://raw.githubusercontent.com/Creators-of-Create/Create/refs/heads/mc1.20.1/0.5.1/src/generated/resources/assets/create/lang/en_us.json').then(x=>x.json())

const files = `
./src/generated/resources/assets/create/lang/en_ud.json
./src/generated/resources/assets/create/lang/en_us.json
./src/main/resources/assets/create/lang/tr_tr.json
./src/main/resources/assets/create/lang/fr_fr.json
./src/main/resources/assets/create/lang/lol_us.json
./src/main/resources/assets/create/lang/ru_ru.json
./src/main/resources/assets/create/lang/es_mx.json
./src/main/resources/assets/create/lang/en_gb.json
./src/main/resources/assets/create/lang/sv_se.json
./src/main/resources/assets/create/lang/pt_pt.json
./src/main/resources/assets/create/lang/default/tooltips.json
./src/main/resources/assets/create/lang/default/interface.json
./src/main/resources/assets/create/lang/ro_ro.json
./src/main/resources/assets/create/lang/de_de.json
./src/main/resources/assets/create/lang/id_id.json
./src/main/resources/assets/create/lang/tok.json
./src/main/resources/assets/create/lang/el_gr.json
./src/main/resources/assets/create/lang/ko_kr.json
./src/main/resources/assets/create/lang/pt_br.json
./src/main/resources/assets/create/lang/fi_fi.json
./src/main/resources/assets/create/lang/be_by.json
./src/main/resources/assets/create/lang/uk_ua.json
./src/main/resources/assets/create/lang/et_ee.json
./src/main/resources/assets/create/lang/eo_uy.json
./src/main/resources/assets/create/lang/zh_cn.json
./src/main/resources/assets/create/lang/eu_es.json
./src/main/resources/assets/create/lang/ja_jp.json
./src/main/resources/assets/create/lang/cs_cz.json
./src/main/resources/assets/create/lang/ksh.json
./src/main/resources/assets/create/lang/cy_gb.json
./src/main/resources/assets/create/lang/hr_hr.json
./src/main/resources/assets/create/lang/vi_vn.json
./src/main/resources/assets/create/lang/ar_sa.json
./src/main/resources/assets/create/lang/pl_pl.json
./src/main/resources/assets/create/lang/lzh.json
./src/main/resources/assets/create/lang/no_no.json
./src/main/resources/assets/create/lang/hy_am.json
./src/main/resources/assets/create/lang/es_es.json
./src/main/resources/assets/create/lang/th_th.json
./src/main/resources/assets/create/lang/da_dk.json
./src/main/resources/assets/create/lang/is_is.json
./src/main/resources/assets/create/lang/bel_tr.json
./src/main/resources/assets/create/lang/fr_ca.json
./src/main/resources/assets/create/lang/zh_tw.json
./src/main/resources/assets/create/lang/fa_ir.json
./src/main/resources/assets/create/lang/hu_hu.json
./src/main/resources/assets/create/lang/rpr.json
./src/main/resources/assets/create/lang/bg_bg.json
./src/main/resources/assets/create/lang/es_cl.json
./src/main/resources/assets/create/lang/it_it.json
./src/main/resources/assets/create/lang/kk_kz.json
./src/main/resources/assets/create/lang/nl_nl.json
`.trim().split('\n').map(x=>x.slice(2))

for (const file of files) {
    console.log(file)
    const data: Record<string, string> = await fetch(`https://raw.githubusercontent.com/Creators-of-Create/Create/refs/heads/mc1.20.1/dev/${file}`).then(x=>x.json())
    for (const key in data051) {
        delete data[key]
    }

    await Deno.mkdir(path.dirname(file), {recursive: true})
    await Deno.writeTextFile(file, JSON.stringify(data))
}
