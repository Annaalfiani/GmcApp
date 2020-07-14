package com.annaalfiani.gmcapps.ui.order

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.annaalfiani.gmcapps.BuildConfig
import com.annaalfiani.gmcapps.models.CreateOrder
import com.annaalfiani.gmcapps.webservices.ApiCllient
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.midtrans.sdk.corekit.models.ItemDetails

class PaymentMidtrans {
    fun initPayment(context: Context, token :String, createOrder: CreateOrder, orderViewModel: OrderViewModel){
        SdkUIFlowBuilder.init().apply {
            setClientKey(BuildConfig.CLIENT_KEY)
            setContext(context)
            enableLog(true)
            setMerchantBaseUrl("${ApiCllient.ENDPOINT}api/snap/")
            setTransactionFinishedCallback{
                it?.let { result ->
                    result.response?.let {
                        when(result.status){
                            TransactionResult.STATUS_FAILED -> {
                                toast(context,"transaction is failed")
                                //orderFragmentViewModel.updatestatus(Constants.getToken(context),id, "failed")
                            }
                            TransactionResult.STATUS_PENDING -> {
                                toast(context,"transaction is pending")
                                orderViewModel.createOrder(token, createOrder)
                                //orderFragmentViewModel.updatestatus(Constants.getToken(context),id, "pending")
                            }
                            TransactionResult.STATUS_SUCCESS -> {
                                toast(context,"transaction is success")
                                orderViewModel.createOrder(token, createOrder)
                                //orderFragmentViewModel.updatestatus(Constants.getToken(context),id, "success")
                            }
                            TransactionResult.STATUS_INVALID -> {
                                toast(context,"transaction is invalid")
                                //orderFragmentViewModel.updatestatus(Constants.getToken(context),id, "invalid")
                            }
                            else -> toast(context,"transaction is invalid")
                        }
                    }
                    if(result.isTransactionCanceled) {
                        toast(context,"Transaction is cancelled")
                    }

                } ?: kotlin.run {
                    toast(context,"Response is null")
                }
            }
        }.buildSDK()
    }

    fun showPayment(context: Context,customer_id: String, harga: Int, total_kursi: Int, film : String) {
        val uiKIt = UIKitCustomSetting().apply {
            isSkipCustomerDetailsPages = true
            isShowPaymentStatus = true
        }
        val midtrans = MidtransSDK.getInstance().apply {
            transactionRequest = setupTransactionRequest(customer_id, harga, total_kursi, film)
            uiKitCustomSetting = uiKIt
        }
        midtrans.startPaymentUiFlow(context)
    }

    private fun setupTransactionRequest(order_id: String, price : Int, qty : Int, name : String) : TransactionRequest {
        val request = TransactionRequest(System.currentTimeMillis().toString(), price.toDouble() * qty)
        val items = ArrayList<ItemDetails>()
        items.add(ItemDetails(order_id, price.toDouble(), qty, name))
        request.itemDetails = items
        return request
    }

    private fun toast(context: Context,message: String) = Toast.makeText(context,message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }
}