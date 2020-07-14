package com.annaalfiani.gmcapps.ui.select_seat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.models.Kursi
import com.annaalfiani.gmcapps.utils.extensions.showInfoAlert
import com.annaalfiani.gmcapps.utils.extensions.toast
import com.murgupluoglu.seatview.Seat
import com.murgupluoglu.seatview.SeatViewListener
import kotlinx.android.synthetic.main.activity_seat.*

class SeatActivity : AppCompatActivity() {
    private lateinit var seatActivityViewModel: SeatActivityViewModel
    private val rowNames: java.util.HashMap<String, String> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat)
        setupViewModel()
        setSeatInfo()
        setupSeatView()
        setupSeatViewBehavior()
        observeSelectedSeat()
        done()
    }

    private fun setupViewModel(){
        seatActivityViewModel = ViewModelProvider(this).get(SeatActivityViewModel::class.java)
    }


    private fun observeSelectedSeat() = seatActivityViewModel.getSelectedSeats().observe(this, Observer {
        handleSelectedSeats(it)
    })

    private fun handleSelectedSeats(selectedSeats: List<com.annaalfiani.gmcapps.models.Seat>){}

    private fun setSeatInfo(){
        seatActivityViewModel.setSeatInfo(getSeats())
    }

    private fun getSeats() = intent.getParcelableExtra<Kursi>("seat_info")!!

    private fun setupSeatView(){
        seatView.config.zoomActive = true
        seatView.config.zoomAfterClickActive = true
        seatView.seatClickListener = object : SeatViewListener {
            override fun seatReleased(releasedSeat: Seat, selectedSeats: java.util.HashMap<String, Seat>) {
                val seat = getSeats().seats.find { it.nama_kursi.equals(releasedSeat.seatName) }!!
                seatActivityViewModel.releaseSeat(seat)
            }

            override fun seatSelected(selectedSeat: Seat, selectedSeats: java.util.HashMap<String, Seat>) {
                val seat = getSeats().seats.find { it.nama_kursi.equals(selectedSeat.seatName) }!!
                seatActivityViewModel.addSelectedSeat(seat)
            }

            override fun canSelectSeat(clickedSeat: Seat, selectedSeats: java.util.HashMap<String, Seat>): Boolean {
                return clickedSeat.type != Seat.TYPE.UNSELECTABLE
            }
        }
    }

    private fun setupSeatViewBehavior(){
        val letterCount = seatActivityViewModel.listenToSeatInfo().value!!.totalRow!!
        val numberSeatCount = seatActivityViewModel.listenToSeatInfo().value!!.totalColumn!!.toInt()
        val seatArray = Array(letterCount) { Array(numberSeatCount) { Seat() } }
        println("Array size ${seatArray.size}")

        val rowArray = seatActivityViewModel.listenToSeatInfo().value!!.seats
        val transformedData = transformData(seatArray, rowNames, rowArray, letterCount, numberSeatCount)


        seatView.initSeatView(transformedData, letterCount, numberSeatCount, rowNames)
    }

    private fun saveRowNames(){
        val grouped = seatActivityViewModel.listenToSeatInfo().value!!.seats.distinctBy { it.nama_kursi.toString().split("-")[1] }
        for( (index, seat) in grouped.withIndex()){
            rowNames.put(index.toString(), seat.nama_kursi.toString().split("-")[1])
        }
    }

    private fun transformData(seatArray: Array<Array<Seat>>, rowNames: HashMap<String, String>, rowArray: List<com.annaalfiani.gmcapps.models.Seat>, rowCount: Int, columnCount: Int): Array<Array<Seat>>{
        saveRowNames()
        var x = 0
        for (i in rowArray){
            println(x)
            val letterPosition = getHashMapKeyByValue(i.nama_kursi.toString().split("-")[1])
            val numberOfSeat = i.nama_kursi.toString().split("-")[0]
            println(i.status.toString())
            val drawable_res = if (i.status.toString().equals("available")){ "seat_available" }else{ "seat_notavailable"}
            val type_seat = if(i.status.toString().equals("available")) {Seat.TYPE.SELECTABLE} else {Seat.TYPE.UNSELECTABLE}
            val movieSeat = Seat().apply {
                id = i.id.toString()
                rowName = i.nama_kursi.toString().substring(0, 1)
                rowIndex = letterPosition.toInt()
                columnIndex =  numberOfSeat.toInt()
                isSelected = false
                type = type_seat
                seatName = i.nama_kursi.toString()
                drawableResourceName = drawable_res
                selectedDrawableResourceName = "seat_selected"
            }
            seatArray[letterPosition.toInt()][numberOfSeat.toInt()-1] = movieSeat
            x++
        }
        return seatArray
    }

    private fun done(){
        fab.setOnClickListener {
            if (seatActivityViewModel.getSelectedSeats().value!!.isEmpty()){
                showInfoAlert(resources.getString(R.string.no_seat_selected))
            }else{
                toast(seatActivityViewModel.getSelectedSeats().value!!.size.toString() + " kursi dipilih")
                val i = Intent()
                val selectedSeats = seatActivityViewModel.getSelectedSeats().value!!
                i.putParcelableArrayListExtra("selected_seats",selectedSeats as ArrayList<out Parcelable>)
                setResult(Activity.RESULT_OK, i)
                finish()
            }
        }
    }

    private fun getHashMapKeyByValue(value: String): String {
        var found = ""
        rowNames.forEach { (key, v) ->
            if(v.equals(value)){
                found = key
                return@forEach
            }
        }
        return found
    }
}
