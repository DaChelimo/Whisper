package com.da_chelimo.compose_ccp.model

import android.content.Context
import android.telephony.TelephonyManager
import com.da_chelimo.compose_ccp.R
import com.da_chelimo.compose_countrycodepicker.libs.Country



object PickerUtils {
    /**
     * [getCountry] Returns the country of the supplied country code.
     * if the country code is empty, the default country is the United States.
     */
    fun String.getCountry(): Country {
        val default = allCountries.first { it.code.lowercase() == "us" }
        return if (this.isNotEmpty()) {
            allCountries.find { it.code.lowercase() == this.lowercase() } ?: default
        } else {
            default
        }
    }

    /**
     * [getDefaultLangCode] Returns the default language code of the device.
     * [context] The context of the activity or fragment.
     */
    fun getDefaultLangCode(context: Context): String {
        return try {
            val localeCode: TelephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val countryCode = localeCode.networkCountryIso
            countryCode.ifEmpty {
                "us"
            }
        } catch (e: Exception) {
            "us"
        }
    }

    /**
     * [searchForAnItem] Returns a list of items that match the search string.
     * [searchStr] The search string.
     */
    fun List<Country>.searchForAnItem(
        searchStr: String,
    ): List<Country> {
        val filteredItems = filter {
            it.name.contains(
                searchStr,
                ignoreCase = true,
            ) ||
                    it.phoneNoCode.contains(
                        searchStr,
                        ignoreCase = true,
                    ) ||
                    it.code.contains(
                        searchStr,
                        ignoreCase = true,
                    )
        }
        println(filteredItems)
        return filteredItems.toList()
    }

    /**
     * [getFlags] Returns the flag of the country.
     * [countryName] The name of the country.
     */
    fun getFlags(countryName: String): Int {
        return when (countryName) {
            "ad" -> R.drawable.ad
            "ae" -> R.drawable.ae
            "af" -> R.drawable.af
            "ag" -> R.drawable.ag
            "ai" -> R.drawable.ai
            "al" -> R.drawable.al
            "am" -> R.drawable.am
            "ao" -> R.drawable.ao
            "aq" -> R.drawable.aq
            "ar" -> R.drawable.ar
            "as" -> R.drawable.`as`
            "at" -> R.drawable.at
            "au" -> R.drawable.au
            "aw" -> R.drawable.aw
            "ax" -> R.drawable.ax
            "az" -> R.drawable.az
            "ba" -> R.drawable.ba
            "bb" -> R.drawable.bb
            "bd" -> R.drawable.bd
            "be" -> R.drawable.be
            "bf" -> R.drawable.bf
            "bg" -> R.drawable.bg
            "bh" -> R.drawable.bh
            "bi" -> R.drawable.bi
            "bj" -> R.drawable.bj
            "bl" -> R.drawable.bl
            "bm" -> R.drawable.bm
            "bn" -> R.drawable.bn
            "bo" -> R.drawable.bo
            "br" -> R.drawable.br
            "bs" -> R.drawable.bs
            "bt" -> R.drawable.bt
            "bw" -> R.drawable.bw
            "by" -> R.drawable.by
            "bz" -> R.drawable.bz
            "ca" -> R.drawable.ca
            "cc" -> R.drawable.cc
            "cd" -> R.drawable.cd
            "cf" -> R.drawable.cf
            "cg" -> R.drawable.cg
            "ch" -> R.drawable.ch
            "ci" -> R.drawable.ci
            "ck" -> R.drawable.ck
            "cl" -> R.drawable.cl
            "cm" -> R.drawable.cm
            "cn" -> R.drawable.cn
            "co" -> R.drawable.co
            "cr" -> R.drawable.cr
            "cu" -> R.drawable.cu
            "cv" -> R.drawable.cv
            "cw" -> R.drawable.cw
            "cx" -> R.drawable.cx
            "cy" -> R.drawable.cy
            "cz" -> R.drawable.cz
            "de" -> R.drawable.de
            "dj" -> R.drawable.dj
            "dk" -> R.drawable.dk
            "dm" -> R.drawable.dm
            "do" -> R.drawable.ic_do
            "dz" -> R.drawable.dz
            "ec" -> R.drawable.ec
            "ee" -> R.drawable.ee
            "eg" -> R.drawable.eg
            "er" -> R.drawable.er
            "es" -> R.drawable.es
            "et" -> R.drawable.et
            "fi" -> R.drawable.fi
            "fj" -> R.drawable.fj
            "fk" -> R.drawable.fk
            "fm" -> R.drawable.fm
            "fo" -> R.drawable.fo
            "fr" -> R.drawable.fr
            "ga" -> R.drawable.ga
            "gb" -> R.drawable.gb
            "gd" -> R.drawable.gd
            "ge" -> R.drawable.ge
            "gf" -> R.drawable.gf
            "gg" -> R.drawable.gg
            "gh" -> R.drawable.gh
            "gi" -> R.drawable.gi
            "gl" -> R.drawable.gl
            "gm" -> R.drawable.gm
            "gn" -> R.drawable.gn
            "gp" -> R.drawable.gp
            "gq" -> R.drawable.gq
            "gr" -> R.drawable.gr
            "gt" -> R.drawable.gt
            "gu" -> R.drawable.gu
            "gw" -> R.drawable.gw
            "gy" -> R.drawable.gy
            "hk" -> R.drawable.hk
            "hn" -> R.drawable.hn
            "hr" -> R.drawable.hr
            "ht" -> R.drawable.ht
            "hu" -> R.drawable.hu
            "id" -> R.drawable.id
            "ie" -> R.drawable.ie
            "il" -> R.drawable.il
            "im" -> R.drawable.im
            "is" -> R.drawable.`is`
            "in" -> R.drawable.`in`
            "io" -> R.drawable.io
            "iq" -> R.drawable.iq
            "ir" -> R.drawable.ir
            "it" -> R.drawable.it
            "je" -> R.drawable.je
            "jm" -> R.drawable.jm
            "jo" -> R.drawable.jo
            "jp" -> R.drawable.jp
            "ke" -> R.drawable.ke
            "kg" -> R.drawable.kg
            "kh" -> R.drawable.kh
            "ki" -> R.drawable.ki
            "km" -> R.drawable.km
            "kn" -> R.drawable.kn
            "kp" -> R.drawable.kp
            "kr" -> R.drawable.kr
            "kw" -> R.drawable.kw
            "ky" -> R.drawable.ky
            "kz" -> R.drawable.kz
            "la" -> R.drawable.la
            "lb" -> R.drawable.lb
            "lc" -> R.drawable.lc
            "li" -> R.drawable.li
            "lk" -> R.drawable.lk
            "lr" -> R.drawable.lr
            "ls" -> R.drawable.ls
            "lt" -> R.drawable.lt
            "lu" -> R.drawable.lu
            "lv" -> R.drawable.lv
            "ly" -> R.drawable.ly
            "ma" -> R.drawable.ma
            "mc" -> R.drawable.mc
            "md" -> R.drawable.md
            "me" -> R.drawable.me
            "mf" -> R.drawable.mf
            "mg" -> R.drawable.mg
            "mh" -> R.drawable.mh
            "mk" -> R.drawable.mk
            "ml" -> R.drawable.ml
            "mm" -> R.drawable.mm
            "mn" -> R.drawable.mn
            "mo" -> R.drawable.mo
            "mp" -> R.drawable.mp
            "mq" -> R.drawable.mq
            "mr" -> R.drawable.mr
            "ms" -> R.drawable.ms
            "mt" -> R.drawable.mt
            "mu" -> R.drawable.mu
            "mv" -> R.drawable.mv
            "mw" -> R.drawable.mw
            "mx" -> R.drawable.mx
            "my" -> R.drawable.my
            "mz" -> R.drawable.mz
            "na" -> R.drawable.na
            "nc" -> R.drawable.nc
            "ne" -> R.drawable.ne
            "nf" -> R.drawable.nf
            "ng" -> R.drawable.ng
            "ni" -> R.drawable.ni
            "nl" -> R.drawable.nl
            "no" -> R.drawable.no
            "np" -> R.drawable.np
            "nr" -> R.drawable.nr
            "nu" -> R.drawable.nu
            "nz" -> R.drawable.nz
            "om" -> R.drawable.om
            "pa" -> R.drawable.pa
            "pe" -> R.drawable.pe
            "pf" -> R.drawable.pf
            "pg" -> R.drawable.pg
            "ph" -> R.drawable.ph
            "pk" -> R.drawable.pk
            "pl" -> R.drawable.pl
            "pm" -> R.drawable.pm
            "pn" -> R.drawable.pn
            "pr" -> R.drawable.pr
            "ps" -> R.drawable.ps
            "pt" -> R.drawable.pt
            "pw" -> R.drawable.pw
            "py" -> R.drawable.py
            "qa" -> R.drawable.qa
            "re" -> R.drawable.re
            "ro" -> R.drawable.ro
            "rs" -> R.drawable.rs
            "ru" -> R.drawable.ru
            "rw" -> R.drawable.rw
            "sa" -> R.drawable.sa
            "sb" -> R.drawable.sb
            "sc" -> R.drawable.sc
            "sd" -> R.drawable.sd
            "se" -> R.drawable.se
            "sg" -> R.drawable.sg
            "sh" -> R.drawable.sh
            "si" -> R.drawable.si
            "sk" -> R.drawable.sk
            "sl" -> R.drawable.sl
            "sm" -> R.drawable.sm
            "sn" -> R.drawable.sn
            "so" -> R.drawable.so
            "sr" -> R.drawable.sr
            "ss" -> R.drawable.ss
            "st" -> R.drawable.st
            "sv" -> R.drawable.sv
            "sx" -> R.drawable.sx
            "sy" -> R.drawable.sy
            "sz" -> R.drawable.sz
            "tc" -> R.drawable.tc
            "td" -> R.drawable.td
            "tg" -> R.drawable.tg
            "th" -> R.drawable.th
            "tj" -> R.drawable.tj
            "tk" -> R.drawable.tk
            "tl" -> R.drawable.tl
            "tm" -> R.drawable.tm
            "tn" -> R.drawable.tn
            "to" -> R.drawable.to
            "tr" -> R.drawable.tr
            "tt" -> R.drawable.tt
            "tv" -> R.drawable.tv
            "tw" -> R.drawable.tw
            "tz" -> R.drawable.tz
            "ua" -> R.drawable.ua
            "ug" -> R.drawable.ug
            "us" -> R.drawable.us
            "uy" -> R.drawable.uy
            "uz" -> R.drawable.uz
            "va" -> R.drawable.va
            "vc" -> R.drawable.vc
            "ve" -> R.drawable.ve
            "vg" -> R.drawable.vg
            "vi" -> R.drawable.vi
            "vn" -> R.drawable.vn
            "vu" -> R.drawable.vu
            "wf" -> R.drawable.wf
            "ws" -> R.drawable.ws
            "xk" -> R.drawable.xk
            "ye" -> R.drawable.ye
            "yt" -> R.drawable.yt
            "za" -> R.drawable.za
            "zm" -> R.drawable.zm
            "zw" -> R.drawable.zw
            else -> R.drawable.tr
        }
    }

    /**
     * [getCountryName] Returns the name of the country.
     * [countryName] The name of the country.
     */
    fun getCountryName(countryName: String): Int {
        return when (countryName) {
            "ad" -> R.string.andorra
            "ae" -> R.string.united_arab_emirates
            "af" -> R.string.afghanistan
            "ag" -> R.string.antigua_and_barbuda
            "ai" -> R.string.anguilla
            "al" -> R.string.albania
            "am" -> R.string.armenia
            "ao" -> R.string.angola
            "aq" -> R.string.antarctica
            "ar" -> R.string.argentina
            "as" -> R.string.american_samoa
            "at" -> R.string.austria
            "au" -> R.string.australia
            "aw" -> R.string.aruba
            "ax" -> R.string.aland_islands
            "az" -> R.string.azerbaijan
            "ba" -> R.string.bosnia
            "bb" -> R.string.barbados
            "bd" -> R.string.bangladesh
            "be" -> R.string.belgium
            "bf" -> R.string.burkina_faso
            "bg" -> R.string.bulgaria
            "bh" -> R.string.bahrain
            "bi" -> R.string.burundi
            "bj" -> R.string.benin
            "bl" -> R.string.saint_barhelemy
            "bm" -> R.string.bermuda
            "bn" -> R.string.brunei_darussalam
            "bo" -> R.string.bolivia
            "br" -> R.string.brazil
            "bs" -> R.string.bahamas
            "bt" -> R.string.bhutan
            "bw" -> R.string.botswana
            "by" -> R.string.belarus
            "bz" -> R.string.belize
            "ca" -> R.string.canada
            "cc" -> R.string.cocos
            "cd" -> R.string.congo_democratic
            "cf" -> R.string.central_african
            "cg" -> R.string.congo
            "ch" -> R.string.switzerland
            "ci" -> R.string.cote_dlvoire
            "ck" -> R.string.cook_islands
            "cl" -> R.string.chile
            "cm" -> R.string.cameroon
            "cn" -> R.string.china
            "co" -> R.string.colombia
            "cr" -> R.string.costa_rica
            "cu" -> R.string.cuba
            "cv" -> R.string.cape_verde
            "cw" -> R.string.curacao
            "cx" -> R.string.christmas_island
            "cy" -> R.string.cyprus
            "cz" -> R.string.czech_republic
            "de" -> R.string.germany
            "dj" -> R.string.djibouti
            "dk" -> R.string.denmark
            "dm" -> R.string.dominica
            "do" -> R.string.dominician_republic
            "dz" -> R.string.algeria
            "ec" -> R.string.ecuador
            "ee" -> R.string.estonia
            "eg" -> R.string.egypt
            "er" -> R.string.eritrea
            "es" -> R.string.spain
            "et" -> R.string.ethiopia
            "fi" -> R.string.finland
            "fj" -> R.string.fiji
            "fk" -> R.string.falkland_islands
            "fm" -> R.string.micro
            "fo" -> R.string.faroe_islands
            "fr" -> R.string.france
            "ga" -> R.string.gabon
            "gb" -> R.string.united_kingdom
            "gd" -> R.string.grenada
            "ge" -> R.string.georgia
            "gf" -> R.string.french_guyana
            "gg" -> R.string.guernsey
            "gh" -> R.string.ghana
            "gi" -> R.string.gibraltar
            "gl" -> R.string.greenland
            "gm" -> R.string.gambia
            "gn" -> R.string.guinea
            "gp" -> R.string.guadeloupe
            "gq" -> R.string.equatorial_guinea
            "gr" -> R.string.greece
            "gt" -> R.string.guatemala
            "gu" -> R.string.guam
            "gw" -> R.string.guinea_bissau
            "gy" -> R.string.guyana
            "hk" -> R.string.hong_kong
            "hn" -> R.string.honduras
            "hr" -> R.string.croatia
            "ht" -> R.string.haiti
            "hu" -> R.string.hungary
            "id" -> R.string.indonesia
            "ie" -> R.string.ireland
            "il" -> R.string.israil
            "im" -> R.string.isle_of_man
            "is" -> R.string.iceland
            "in" -> R.string.india
            "io" -> R.string.british_indian_ocean
            "iq" -> R.string.iraq
            "ir" -> R.string.iran
            "it" -> R.string.italia
            "je" -> R.string.jersey
            "jm" -> R.string.jamaica
            "jo" -> R.string.jordan
            "jp" -> R.string.japan
            "ke" -> R.string.kenya
            "kg" -> R.string.kyrgyzstan
            "kh" -> R.string.cambodia
            "ki" -> R.string.kiribati
            "km" -> R.string.comoros
            "kn" -> R.string.saint_kitts
            "kp" -> R.string.north_korea
            "kr" -> R.string.south_korea
            "kw" -> R.string.kuwait
            "ky" -> R.string.cayman_islands
            "kz" -> R.string.kazakhstan
            "la" -> R.string.laos
            "lb" -> R.string.lebanon
            "lc" -> R.string.saint_lucia
            "li" -> R.string.liechtenstein
            "lk" -> R.string.siri_lanka
            "lr" -> R.string.liberia
            "ls" -> R.string.lesotho
            "lt" -> R.string.lithuania
            "lu" -> R.string.luxembourg
            "lv" -> R.string.latvia
            "ly" -> R.string.libya
            "ma" -> R.string.marocco
            "mc" -> R.string.monaco
            "md" -> R.string.moldova
            "me" -> R.string.montenegro
            "mf" -> R.string.saint_martin
            "mg" -> R.string.madagascar
            "mh" -> R.string.marshall_islands
            "mk" -> R.string.north_macedonia
            "ml" -> R.string.mali
            "mm" -> R.string.myanmar
            "mn" -> R.string.mongolia
            "mo" -> R.string.macau
            "mp" -> R.string.northern_mariana
            "mq" -> R.string.martinique
            "mr" -> R.string.mauriatana
            "ms" -> R.string.montserrat
            "mt" -> R.string.malta
            "mu" -> R.string.mauritius
            "mv" -> R.string.maldives
            "mw" -> R.string.malawi
            "mx" -> R.string.mexico
            "my" -> R.string.malaysia
            "mz" -> R.string.mozambique
            "na" -> R.string.namibia
            "nc" -> R.string.new_caledonia
            "ne" -> R.string.niger
            "nf" -> R.string.norfolk
            "ng" -> R.string.nigeria
            "ni" -> R.string.nicaragua
            "nl" -> R.string.netherlands
            "no" -> R.string.norway
            "np" -> R.string.nepal
            "nr" -> R.string.nauru
            "nu" -> R.string.niue
            "nz" -> R.string.new_zealand
            "om" -> R.string.oman
            "pa" -> R.string.panama
            "pe" -> R.string.peru
            "pf" -> R.string.french_polynesia
            "pg" -> R.string.papua_new_guinea
            "ph" -> R.string.philippinies
            "pk" -> R.string.pakistan
            "pl" -> R.string.poland
            "pm" -> R.string.saint_pierre
            "pn" -> R.string.pitcairn
            "pr" -> R.string.puerto_rico
            "ps" -> R.string.state_of_palestine
            "pt" -> R.string.portugal
            "pw" -> R.string.palau
            "py" -> R.string.paraguay
            "qa" -> R.string.qatar
            "re" -> R.string.reunion
            "ro" -> R.string.romania
            "rs" -> R.string.serbia
            "ru" -> R.string.russia
            "rw" -> R.string.rwanda
            "sa" -> R.string.saudi_arabia
            "sb" -> R.string.solomon_islands
            "sc" -> R.string.seychelles
            "sd" -> R.string.sudan
            "se" -> R.string.sweden
            "sg" -> R.string.singapore
            "sh" -> R.string.saint_helena
            "si" -> R.string.slovenia
            "sk" -> R.string.slovakia
            "sl" -> R.string.sierra_leone
            "sm" -> R.string.san_marino
            "sn" -> R.string.senegal
            "so" -> R.string.somali
            "sr" -> R.string.suriname
            "ss" -> R.string.south_sudan
            "st" -> R.string.sao_tome
            "sv" -> R.string.el_salvador
            "sx" -> R.string.sint_maarten
            "sy" -> R.string.syrian
            "sz" -> R.string.swaziland
            "tc" -> R.string.turks_and_caicos
            "td" -> R.string.chad
            "tg" -> R.string.togo
            "th" -> R.string.thailand
            "tj" -> R.string.taijikistan
            "tk" -> R.string.tokelau
            "tl" -> R.string.timor_leste
            "tm" -> R.string.turkmenistan
            "tn" -> R.string.tunisia
            "to" -> R.string.tonga
            "tr" -> R.string.turkey
            "tt" -> R.string.trinidad_and_tobago
            "tv" -> R.string.tuvalu
            "tw" -> R.string.taiwan
            "tz" -> R.string.tazmania
            "ua" -> R.string.ukraina
            "ug" -> R.string.uganda
            "us" -> R.string.united_states_america
            "uy" -> R.string.uruguay
            "uz" -> R.string.uzbekistan
            "va" -> R.string.holy_see
            "vc" -> R.string.saint_vincent
            "ve" -> R.string.venezuela
            "vg" -> R.string.virgin_islands
            "vi" -> R.string.virgin_island_us
            "vn" -> R.string.vietnam
            "vu" -> R.string.vanuatu
            "wf" -> R.string.walli_and_fatuna
            "ws" -> R.string.samoa
            "xk" -> R.string.kosovo
            "ye" -> R.string.yemen
            "yt" -> R.string.mayotte
            "za" -> R.string.south_africa
            "zm" -> R.string.zambia
            "zw" -> R.string.zimbabwe
            else -> R.string.kenya
        }
    }

    /**
     * [allCountries] is a list of all countries in the world sorted alphabetically.
     */
    val allCountries: List<Country>
        get() = listOf(
            Country(
                code = "ad",
                phoneNoCode = "+376",
                name = "Andorra",
                flag = R.drawable.ad,
            ),
            Country(
                code = "ae",
                phoneNoCode = "+971",
                name = "United Arab Emirates (UAE)",
                flag = R.drawable.ae,
            ),
            Country(
                code = "af",
                phoneNoCode = "+93",
                name = "Afghanistan",
                flag = R.drawable.af,
            ),
            Country(
                code = "ag",
                phoneNoCode = "+1",
                name = "Antigua and Barbuda",
                flag = R.drawable.ag,
            ),
            Country(
                code = "ai",
                phoneNoCode = "+1",
                name = "Anguilla",
                flag = R.drawable.ai,
            ),
            Country(
                "al",
                "+355",
                "Albania",
                R.drawable.al,
            ),
            Country(
                "am",
                "+374",
                "Armenia",
                R.drawable.am,
            ),
            Country(
                "ao",
                "+244",
                "Angola",
                R.drawable.ao,
            ),
            Country(
                "aq",
                "+672",
                "Antarctica",
                R.drawable.aq,
            ),
            Country(
                "ar",
                "+54",
                "Argentina",
                R.drawable.ar,
            ),
            Country(
                "as",
                "+1",
                "American Samoa",
                R.drawable.`as`,
            ),
            Country(
                "at",
                "+43",
                "Austria",
                R.drawable.at,
            ),
            Country(
                "au",
                "+61",
                "Australia",
                R.drawable.au,
            ),
            Country(
                "aw",
                "+297",
                "Aruba",
                R.drawable.aw,
            ),
            Country(
                "ax",
                "+358",
                "Åland Islands",
                R.drawable.ax,
            ),
            Country(
                "az",
                "+994",
                "Azerbaijan",
                R.drawable.az,
            ),
            Country(
                "ba",
                "+387",
                "Bosnia And Herzegovina",
                R.drawable.ba,
            ),
            Country(
                "bb",
                "+1",
                "Barbados",
                R.drawable.bb,
            ),
            Country(
                "bd",
                "+880",
                "Bangladesh",
                R.drawable.bd,
            ),
            Country(
                "be",
                "+32",
                "Belgium",
                R.drawable.be,
            ),
            Country(
                "bf",
                "+226",
                "Burkina Faso",
                R.drawable.bf,
            ),
            Country(
                "bg",
                "+359",
                "Bulgaria",
                R.drawable.bg,
            ),
            Country(
                "bh",
                "+973",
                "Bahrain",
                R.drawable.bh,
            ),
            Country(
                "bi",
                "+257",
                "Burundi",
                R.drawable.bi,
            ),
            Country(
                "bj",
                "+229",
                "Benin",
                R.drawable.bj,
            ),
            Country(
                "bl",
                "+590",
                "Saint Barthélemy",
                R.drawable.bl,
            ),
            Country(
                "bm",
                "+1",
                "Bermuda",
                R.drawable.bm,
            ),
            Country(
                "bn",
                "+673",
                "Brunei Darussalam",
                R.drawable.bn,
            ),
            Country(
                "bo",
                "+591",
                "Bolivia, Plurinational State Of",
                R.drawable.bo,
            ),
            Country(
                "br",
                "+55",
                "Brazil",
                R.drawable.br,
            ),
            Country(
                "bs",
                "+1",
                "Bahamas",
                R.drawable.bs,
            ),
            Country(
                "bt",
                "+975",
                "Bhutan",
                R.drawable.bt,
            ),
            Country(
                "bw",
                "+267",
                "Botswana",
                R.drawable.bw,
            ),
            Country(
                "by",
                "+375",
                "Belarus",
                R.drawable.by,
            ),
            Country(
                "bz",
                "+501",
                "Belize",
                R.drawable.bz,
            ),
            Country(
                "ca",
                "+1",
                "Canada",
                R.drawable.ca,
            ),
            Country(
                "cc",
                "+61",
                "Cocos (keeling) Islands",
                R.drawable.cc,
            ),
            Country(
                "cd",
                "+243",
                "Congo, The Democratic Republic Of The",
                R.drawable.cd,
            ),
            Country(
                "cf",
                "+236",
                "Central African Republic",
                R.drawable.cf,
            ),
            Country(
                "cg",
                "+242",
                "Congo",
                R.drawable.cg,
            ),
            Country(
                "ch",
                "+41",
                "Switzerland",
                R.drawable.ch,
            ),
            Country(
                "ci",
                "+225",
                "Côte D'ivoire",
                R.drawable.ci,
            ),
            Country(
                "ck",
                "+682",
                "Cook Islands",
                R.drawable.ck,
            ),
            Country(
                "cl",
                "+56",
                "Chile",
                R.drawable.cl,
            ),
            Country(
                "cm",
                "+237",
                "Cameroon",
                R.drawable.cm,
            ),
            Country(
                "cn",
                "+86",
                "China",
                R.drawable.cn,
            ),
            Country(
                "co",
                "+57",
                "Colombia",
                R.drawable.co,
            ),
            Country(
                "cr",
                "+506",
                "Costa Rica",
                R.drawable.cr,
            ),
            Country(
                "cu",
                "+53",
                "Cuba",
                R.drawable.cu,
            ),
            Country(
                "cv",
                "+238",
                "Cape Verde",
                R.drawable.cv,
            ),
            Country(
                "cw",
                "+599",
                "Curaçao",
                R.drawable.cw,
            ),
            Country(
                "cx",
                "+61",
                "Christmas Island",
                R.drawable.cx,
            ),
            Country(
                "cy",
                "+357",
                "Cyprus",
                R.drawable.cy,
            ),
            Country(
                "cz",
                "+420",
                "Czech Republic",
                R.drawable.cz,
            ),
            Country(
                "de",
                "+49",
                "Germany",
                R.drawable.de,
            ),
            Country(
                "dj",
                "+253",
                "Djibouti",
                R.drawable.dj,
            ),
            Country(
                "dk",
                "+45",
                "Denmark",
                R.drawable.dk,
            ),
            Country(
                "dm",
                "+1",
                "Dominica",
                R.drawable.dm,
            ),
            Country(
                "do",
                "+1",
                "Dominican Republic",
                R.drawable.ic_do,
            ),
            Country(
                "dz",
                "+213",
                "Algeria",
                R.drawable.dz,
            ),
            Country(
                "ec",
                "+593",
                "Ecuador",
                R.drawable.ec,
            ),
            Country(
                "ee",
                "+372",
                "Estonia",
                R.drawable.ee,
            ),
            Country(
                "eg",
                "+20",
                "Egypt",
                R.drawable.eg,
            ),
            Country(
                "er",
                "+291",
                "Eritrea",
                R.drawable.er,
            ),
            Country(
                "es",
                "+34",
                "Spain",
                R.drawable.es,
            ),
            Country(
                "et",
                "+251",
                "Ethiopia",
                R.drawable.et,
            ),
            Country(
                "fi",
                "+358",
                "Finland",
                R.drawable.fi,
            ),
            Country(
                "fj",
                "+679",
                "Fiji",
                R.drawable.fj,
            ),
            Country(
                "fk",
                "+500",
                "Falkland Islands (malvinas)",
                R.drawable.fk,
            ),
            Country(
                "fm",
                "+691",
                "Micronesia, Federated States Of",
                R.drawable.fm,
            ),
            Country(
                "fo",
                "+298",
                "Faroe Islands",
                R.drawable.fo,
            ),
            Country(
                "fr",
                "+33",
                "France",
                R.drawable.fr,
            ),
            Country(
                "ga",
                "+241",
                "Gabon",
                R.drawable.ga,
            ),
            Country(
                "gb",
                "+44",
                "United Kingdom",
                R.drawable.gb,
            ),
            Country(
                "gd",
                "+1",
                "Grenada",
                R.drawable.gd,
            ),
            Country(
                "ge",
                "+995",
                "Georgia",
                R.drawable.ge,
            ),
            Country(
                "gf",
                "+594",
                "French Guyana",
                R.drawable.gf,
            ),
            Country(
                "gh",
                "+233",
                "Ghana",
                R.drawable.gh,
            ),
            Country(
                "gi",
                "+350",
                "Gibraltar",
                R.drawable.gi,
            ),
            Country(
                "gl",
                "+299",
                "Greenland",
                R.drawable.gl,
            ),
            Country(
                "gm",
                "+220",
                "Gambia",
                R.drawable.gm,
            ),
            Country(
                "gn",
                "+224",
                "Guinea",
                R.drawable.gn,
            ),
            Country(
                "gp",
                "+450",
                "Guadeloupe",
                R.drawable.gp,
            ),
            Country(
                "gq",
                "+240",
                "Equatorial Guinea",
                R.drawable.gq,
            ),
            Country(
                "gr",
                "+30",
                "Greece",
                R.drawable.gr,
            ),
            Country(
                "gt",
                "+502",
                "Guatemala",
                R.drawable.gt,
            ),
            Country(
                "gu",
                "+1",
                "Guam",
                R.drawable.gu,
            ),
            Country(
                "gw",
                "+245",
                "Guinea-bissau",
                R.drawable.gw,
            ),
            Country(
                "gy",
                "+592",
                "Guyana",
                R.drawable.gy,
            ),
            Country(
                "hk",
                "+852",
                "Hong Kong",
                R.drawable.hk,
            ),
            Country(
                "hn",
                "+504",
                "Honduras",
                R.drawable.hn,
            ),
            Country(
                "hr",
                "+385",
                "Croatia",
                R.drawable.hr,
            ),
            Country(
                "ht",
                "+509",
                "Haiti",
                R.drawable.ht,
            ),
            Country(
                "hu",
                "+36",
                "Hungary",
                R.drawable.hu,
            ),
            Country(
                "id",
                "+62",
                "Indonesia",
                R.drawable.id,
            ),
            Country(
                "ie",
                "+353",
                "Ireland",
                R.drawable.ie,
            ),
            Country(
                "il",
                "+972",
                "Israel",
                R.drawable.il,
            ),
            Country(
                "im",
                "+44",
                "Isle Of Man",
                R.drawable.im,
            ),
            Country(
                "is",
                "+354",
                "Iceland",
                R.drawable.`is`,
            ),
            Country(
                "in",
                "+91",
                "India",
                R.drawable.`in`,
            ),
            Country(
                "io",
                "+246",
                "British Indian Ocean Territory",
                R.drawable.io,
            ),
            Country(
                "iq",
                "+964",
                "Iraq",
                R.drawable.iq,
            ),
            Country(
                "ir",
                "+98",
                "Iran, Islamic Republic Of",
                R.drawable.ir,
            ),
            Country(
                "it",
                "+39",
                "Italy",
                R.drawable.it,
            ),
            Country(
                "je",
                "+44",
                "Jersey ",
                R.drawable.je,
            ),
            Country(
                "jm",
                "+1",
                "Jamaica",
                R.drawable.jm,
            ),
            Country(
                "jo",
                "+962",
                "Jordan",
                R.drawable.jo,
            ),
            Country(
                "jp",
                "+81",
                "Japan",
                R.drawable.jp,
            ),
            Country(
                "ke",
                "+254",
                "Kenya",
                R.drawable.ke,
            ),
            Country(
                "kg",
                "+996",
                "Kyrgyzstan",
                R.drawable.kg,
            ),
            Country(
                "kh",
                "+855",
                "Cambodia",
                R.drawable.kh,
            ),
            Country(
                "ki",
                "+686",
                "Kiribati",
                R.drawable.ki,
            ),
            Country(
                "km",
                "+269",
                "Comoros",
                R.drawable.km,
            ),
            Country(
                "kn",
                "+1",
                "Saint Kitts and Nevis",
                R.drawable.kn,
            ),
            Country(
                "kp",
                "+850",
                "North Korea",
                R.drawable.kp,
            ),
            Country(
                "kr",
                "+82",
                "South Korea",
                R.drawable.kr,
            ),
            Country(
                "kw",
                "+965",
                "Kuwait",
                R.drawable.kw,
            ),
            Country(
                "ky",
                "+1",
                "Cayman Islands",
                R.drawable.ky,
            ),
            Country(
                "kz",
                "+7",
                "Kazakhstan",
                R.drawable.kz,
            ),
            Country(
                "la",
                "+856",
                "Lao People's Democratic Republic",
                R.drawable.la,
            ),
            Country(
                "lb",
                "+961",
                "Lebanon",
                R.drawable.lb,
            ),
            Country(
                "lc",
                "+1",
                "Saint Lucia",
                R.drawable.lc,
            ),
            Country(
                "li",
                "+423",
                "Liechtenstein",
                R.drawable.li,
            ),
            Country(
                "lk",
                "+94",
                "Sri Lanka",
                R.drawable.lk,
            ),
            Country(
                "lr",
                "+231",
                "Liberia",
                R.drawable.lr,
            ),
            Country(
                "ls",
                "+266",
                "Lesotho",
                R.drawable.ls,
            ),
            Country(
                "lt",
                "+370",
                "Lithuania",
                R.drawable.lt,
            ),
            Country(
                "lu",
                "+352",
                "Luxembourg",
                R.drawable.lu,
            ),
            Country(
                "lv",
                "+371",
                "Latvia",
                R.drawable.lv,
            ),
            Country(
                "ly",
                "+218",
                "Libya",
                R.drawable.ly,
            ),
            Country(
                "ma",
                "+212",
                "Morocco",
                R.drawable.ma,
            ),
            Country(
                "mc",
                "+377",
                "Monaco",
                R.drawable.mc,
            ),
            Country(
                "md",
                "+373",
                "Moldova, Republic Of",
                R.drawable.md,
            ),
            Country(
                "me",
                "+382",
                "Montenegro",
                R.drawable.me,
            ),
            Country(
                "mf",
                "+590",
                "Saint Martin",
                R.drawable.mf,
            ),
            Country(
                "mg",
                "+261",
                "Madagascar",
                R.drawable.mg,
            ),
            Country(
                "mh",
                "+692",
                "Marshall Islands",
                R.drawable.mh,
            ),
            Country(
                "mk",
                "+389",
                "Macedonia (FYROM)",
                R.drawable.mk,
            ),
            Country(
                "ml",
                "+223",
                "Mali",
                R.drawable.ml,
            ),
            Country(
                "mm",
                "+95",
                "Myanmar",
                R.drawable.mm,
            ),
            Country(
                "mn",
                "+976",
                "Mongolia",
                R.drawable.mn,
            ),
            Country(
                "mo",
                "+853",
                "Macau",
                R.drawable.mo,
            ),
            Country(
                "mp",
                "+1",
                "Northern Mariana Islands",
                R.drawable.mp,
            ),
            Country(
                "mq",
                "+596",
                "Martinique",
                R.drawable.mq,
            ),
            Country(
                "mr",
                "+222",
                "Mauritania",
                R.drawable.mr,
            ),
            Country(
                "ms",
                "+1",
                "Montserrat",
                R.drawable.ms,
            ),
            Country(
                "mt",
                "+356",
                "Malta",
                R.drawable.mt,
            ),
            Country(
                "mu",
                "+230",
                "Mauritius",
                R.drawable.mu,
            ),
            Country(
                "mv",
                "+960",
                "Maldives",
                R.drawable.mv,
            ),
            Country(
                "mw",
                "+265",
                "Malawi",
                R.drawable.mw,
            ),
            Country(
                "mx",
                "+52",
                "Mexico",
                R.drawable.mx,
            ),
            Country(
                "my",
                "+60",
                "Malaysia",
                R.drawable.my,
            ),
            Country(
                "mz",
                "+258",
                "Mozambique",
                R.drawable.mz,
            ),
            Country(
                "na",
                "+264",
                "Namibia",
                R.drawable.na,
            ),
            Country(
                "nc",
                "+687",
                "New Caledonia",
                R.drawable.nc,
            ),
            Country(
                "ne",
                "+227",
                "Niger",
                R.drawable.ne,
            ),
            Country(
                "nf",
                "+672",
                "Norfolk Islands",
                R.drawable.nf,
            ),
            Country(
                "ng",
                "+234",
                "Nigeria",
                R.drawable.ng,
            ),
            Country(
                "ni",
                "+505",
                "Nicaragua",
                R.drawable.ni,
            ),
            Country(
                "nl",
                "+31",
                "Netherlands",
                R.drawable.nl,
            ),
            Country(
                "no",
                "+47",
                "Norway",
                R.drawable.no,
            ),
            Country(
                "np",
                "+977",
                "Nepal",
                R.drawable.np,
            ),
            Country(
                "nr",
                "+674",
                "Nauru",
                R.drawable.nr,
            ),
            Country(
                "nu",
                "+683",
                "Niue",
                R.drawable.nu,
            ),
            Country(
                "nz",
                "+64",
                "New Zealand",
                R.drawable.nz,
            ),
            Country(
                "om",
                "+968",
                "Oman",
                R.drawable.om,
            ),
            Country(
                "pa",
                "+507",
                "Panama",
                R.drawable.pa,
            ),
            Country(
                "pe",
                "+51",
                "Peru",
                R.drawable.pe,
            ),
            Country(
                "pf",
                "+689",
                "French Polynesia",
                R.drawable.pf,
            ),
            Country(
                "pg",
                "+675",
                "Papua New Guinea",
                R.drawable.pg,
            ),
            Country(
                "ph",
                "+63",
                "Philippines",
                R.drawable.ph,
            ),
            Country(
                "pk",
                "+92",
                "Pakistan",
                R.drawable.pk,
            ),
            Country(
                "pl",
                "+48",
                "Poland",
                R.drawable.pl,
            ),
            Country(
                "pm",
                "+508",
                "Saint Pierre And Miquelon",
                R.drawable.pm,
            ),
            Country(
                "pn",
                "+870",
                "Pitcairn Islands",
                R.drawable.pn,
            ),
            Country(
                "pr",
                "+1",
                "Puerto Rico",
                R.drawable.pr,
            ),
            Country(
                "ps",
                "+970",
                "Palestine",
                R.drawable.ps,
            ),
            Country(
                "pt",
                "+351",
                "Portugal",
                R.drawable.pt,
            ),
            Country(
                "pw",
                "+680",
                "Palau",
                R.drawable.pw,
            ),
            Country(
                "py",
                "+595",
                "Paraguay",
                R.drawable.py,
            ),
            Country(
                "qa",
                "+974",
                "Qatar",
                R.drawable.qa,
            ),
            Country(
                "re",
                "+262",
                "Réunion",
                R.drawable.re,
            ),
            Country(
                "ro",
                "+40",
                "Romania",
                R.drawable.ro,
            ),
            Country(
                "rs",
                "+381",
                "Serbia",
                R.drawable.rs,
            ),
            Country(
                "ru",
                "+7",
                "Russian Federation",
                R.drawable.ru,
            ),
            Country(
                "rw",
                "+250",
                "Rwanda",
                R.drawable.rw,
            ),
            Country(
                "sa",
                "+966",
                "Saudi Arabia",
                R.drawable.sa,
            ),
            Country(
                "sb",
                "+677",
                "Solomon Islands",
                R.drawable.sb,
            ),
            Country(
                "sc",
                "+248",
                "Seychelles",
                R.drawable.sc,
            ),
            Country(
                "sd",
                "+249",
                "Sudan",
                R.drawable.sd,
            ),
            Country(
                "se",
                "+46",
                "Sweden",
                R.drawable.se,
            ),
            Country(
                "sg",
                "+65",
                "Singapore",
                R.drawable.sg,
            ),
            Country(
                "sh",
                "+290",
                "Saint Helena, Ascension And Tristan Da Cunha",
                R.drawable.sh,
            ),
            Country(
                "si",
                "+386",
                "Slovenia",
                R.drawable.si,
            ),
            Country(
                "sk",
                "+421",
                "Slovakia",
                R.drawable.sk,
            ),
            Country(
                "sl",
                "+232",
                "Sierra Leone",
                R.drawable.sl,
            ),
            Country(
                "sm",
                "+378",
                "San Marino",
                R.drawable.sm,
            ),
            Country(
                "sn",
                "+221",
                "Senegal",
                R.drawable.sn,
            ),
            Country(
                "so",
                "+252",
                "Somalia",
                R.drawable.so,
            ),
            Country(
                "sr",
                "+597",
                "Suriname",
                R.drawable.sr,
            ),
            Country(
                "ss",
                "+211",
                "South Sudan",
                R.drawable.ss,
            ),
            Country(
                "st",
                "+239",
                "Sao Tome And Principe",
                R.drawable.st,
            ),
            Country(
                "sv",
                "+503",
                "El Salvador",
                R.drawable.sv,
            ),
            Country(
                "sx",
                "+1",
                "Sint Maarten",
                R.drawable.sx,
            ),
            Country(
                "sy",
                "+963",
                "Syrian Arab Republic",
                R.drawable.sy,
            ),
            Country(
                "sz",
                "+268",
                "Swaziland",
                R.drawable.sz,
            ),
            Country(
                "tc",
                "+1",
                "Turks and Caicos Islands",
                R.drawable.tc,
            ),
            Country(
                "td",
                "+235",
                "Chad",
                R.drawable.td,
            ),
            Country(
                "tg",
                "+228",
                "Togo",
                R.drawable.tg,
            ),
            Country(
                "th",
                "+66",
                "Thailand",
                R.drawable.th,
            ),
            Country(
                "tj",
                "+992",
                "Tajikistan",
                R.drawable.tj,
            ),
            Country(
                "tk",
                "+690",
                "Tokelau",
                R.drawable.tk,
            ),
            Country(
                "tl",
                "+670",
                "Timor-leste",
                R.drawable.tl,
            ),
            Country(
                "tm",
                "+993",
                "Turkmenistan",
                R.drawable.tm,
            ),
            Country(
                "tn",
                "+216",
                "Tunisia",
                R.drawable.tn,
            ),
            Country(
                "to",
                "+676",
                "Tonga",
                R.drawable.to,
            ),
            Country(
                "tr",
                "+90",
                "Turkey",
                R.drawable.tr,
            ),
            Country(
                "tt",
                "+1",
                "Trinidad &amp; Tobago",
                R.drawable.tt,
            ),
            Country(
                "tv",
                "+688",
                "Tuvalu",
                R.drawable.tv,
            ),
            Country(
                "tw",
                "+886",
                "Taiwan",
                R.drawable.tw,
            ),
            Country(
                "tz",
                "+255",
                "Tanzania, United Republic Of",
                R.drawable.tz,
            ),
            Country(
                "ua",
                "+380",
                "Ukraine",
                R.drawable.ua,
            ),
            Country(
                "ug",
                "+256",
                "Uganda",
                R.drawable.ug,
            ),
            Country(
                "us",
                "+1",
                "United States",
                R.drawable.us,
            ),
            Country(
                "uy",
                "+598",
                "Uruguay",
                R.drawable.uy,
            ),
            Country(
                "uz",
                "+998",
                "Uzbekistan",
                R.drawable.uz,
            ),
            Country(
                "va",
                "+379",
                "Holy See (vatican City State)",
                R.drawable.va,
            ),
            Country(
                "vc",
                "+1",
                "Saint Vincent &amp; The Grenadines",
                R.drawable.vc,
            ),
            Country(
                "ve",
                "+58",
                "Venezuela, Bolivarian Republic Of",
                R.drawable.ve,
            ),
            Country(
                "vg",
                "+1",
                "British Virgin Islands",
                R.drawable.vg,
            ),
            Country(
                "vi",
                "+1",
                "US Virgin Islands",
                R.drawable.vi,
            ),
            Country(
                "vn",
                "+84",
                "Vietnam",
                R.drawable.vn,
            ),
            Country(
                "vu",
                "+678",
                "Vanuatu",
                R.drawable.vu,
            ),
            Country(
                "wf",
                "+681",
                "Wallis And Futuna",
                R.drawable.wf,
            ),
            Country(
                "ws",
                "4685",
                "Samoa",
                R.drawable.ws,
            ),
            Country(
                "xk",
                "+383",
                "Kosovo",
                R.drawable.xk,
            ),
            Country(
                "ye",
                "+967",
                "Yemen",
                R.drawable.ye,
            ),
            Country(
                "yt",
                "+262",
                "Mayotte",
                R.drawable.yt,
            ),
            Country(
                "za",
                "+27",
                "South Africa",
                R.drawable.za,
            ),
            Country(
                "zm",
                "+260",
                "Zambia",
                R.drawable.zm,
            ),
            Country(
                "zw",
                "+263",
                "Zimbabwe",
                R.drawable.zw,
            ),
        ).sortedBy { it.name }
}