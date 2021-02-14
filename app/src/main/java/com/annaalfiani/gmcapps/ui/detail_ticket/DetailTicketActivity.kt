package com.annaalfiani.gmcapps.ui.detail_ticket

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.api.load
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.models.OrderDetails
import com.annaalfiani.gmcapps.utils.Utilities
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_detail_ticket.*

class DetailTicketActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_ticket)

        getPassedOrderDetails()?.let {
            iv_poster_image.load(it.order.film.foto)
            txt_judul.text = it.order.film.judul
            txt_genre.text = it.order.film.genre
            txt_kursi.text = "${it.kursi.nama_kursi} Kursi"

            txt_date.text = it.order.tanggal
            txt_hour.text = "${it.order.jam} WIB"
            txt_cinema.text = "${it.order.studio.nama}"
            txt_harga.text = Utilities.setToIDR(it.price!!)
        }

        generateQRCode()
    }

    private fun generateQRCode(){
        getPassedOrderDetails()?.let {
            val id = it.id.toString()
            val x = QRCodeWriter().encode(id, BarcodeFormat.QR_CODE, 700, 700)
            qrcode_imageView.load(Utilities.createBitmap(x))
        }
    }

    private fun getPassedOrderDetails() : OrderDetails? = intent.getParcelableExtra("ORDER_DETAILS")
}
