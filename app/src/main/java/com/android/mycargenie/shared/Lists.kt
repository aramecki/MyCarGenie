package com.android.mycargenie.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.android.mycargenie.R

object Brands {
    @Composable
    fun getBrandsList(): List<String> {
        return listOf(
            stringResource(R.string.abarth),
            stringResource(R.string.alfa_romeo),
            stringResource(R.string.aston_martin),
            stringResource(R.string.audi),
            stringResource(R.string.bentley),
            stringResource(R.string.bmw),
            stringResource(R.string.cadillac),
            stringResource(R.string.chevrolet),
            stringResource(R.string.chrysler),
            stringResource(R.string.citroen),
            stringResource(R.string.cupra),
            stringResource(R.string.dacia),
            stringResource(R.string.daihatsu),
            stringResource(R.string.dodge),
            stringResource(R.string.ds),
            stringResource(R.string.ferrari),
            stringResource(R.string.fiat),
            stringResource(R.string.ford),
            stringResource(R.string.genesis),
            stringResource(R.string.honda),
            stringResource(R.string.hyundai),
            stringResource(R.string.infiniti),
            stringResource(R.string.jaguar),
            stringResource(R.string.jeep),
            stringResource(R.string.kia),
            stringResource(R.string.lamborghini),
            stringResource(R.string.lancia),
            stringResource(R.string.land_rover),
            stringResource(R.string.lexus),
            stringResource(R.string.maserati),
            stringResource(R.string.mazda),
            stringResource(R.string.mercedes_benz),
            stringResource(R.string.mini),
            stringResource(R.string.mitsubishi),
            stringResource(R.string.nissan),
            stringResource(R.string.opel),
            stringResource(R.string.peugeot),
            stringResource(R.string.porsche),
            stringResource(R.string.renault),
            stringResource(R.string.rolls_royce),
            stringResource(R.string.seat),
            stringResource(R.string.skoda),
            stringResource(R.string.smart),
            stringResource(R.string.ssangyong),
            stringResource(R.string.subaru),
            stringResource(R.string.suzuki),
            stringResource(R.string.tesla),
            stringResource(R.string.toyota),
            stringResource(R.string.volkswagen),
            stringResource(R.string.volvo)
        )
    }
}


object CarTypes {
    @Composable
    fun getCarTypesList(): List<String> {
        return listOf(
            stringResource(R.string.sedan),
            stringResource(R.string.coupe),
            stringResource(R.string.sportscar),
            stringResource(R.string.suv),
            stringResource(R.string.stationwagon),
            stringResource(R.string.minivan),
            stringResource(R.string.supercar),
            stringResource(R.string.different)
        )
    }
}

object CarFuels {
    @Composable
    fun getCarFuelsList(): List<String> {
        return listOf(
            stringResource(R.string.gasoline),
            stringResource(R.string.diesel),
            stringResource(R.string.lpg),
            stringResource(R.string.cng),
            stringResource(R.string.electric),
            stringResource(R.string.different)
        )
    }
}

object CarEcoList {
    @Composable
    fun getCarEcoList(): List<String> {
        return listOf(
            stringResource(R.string.e1),
            stringResource(R.string.e2),
            stringResource(R.string.e3),
            stringResource(R.string.e4),
            stringResource(R.string.e5),
            stringResource(R.string.e6),
            stringResource(R.string.different)
        )
    }
}