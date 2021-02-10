package com.annaalfiani.gmcapps.ui.detail_film

import com.annaalfiani.gmcapps.models.Studio
import com.annaalfiani.gmcapps.models.v2.Cinema
import com.annaalfiani.gmcapps.models.v2.Hours
import com.annaalfiani.gmcapps.models.v2.Schedulle

interface DetailFilmListener{
    fun clickSchedulle(schedulle: Schedulle)
    fun clickCinema(cinema: Cinema)
    fun clickHour(hour: Hours)
}