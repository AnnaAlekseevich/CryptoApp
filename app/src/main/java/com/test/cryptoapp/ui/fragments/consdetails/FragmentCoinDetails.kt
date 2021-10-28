package com.test.cryptoapp.ui.fragments.consdetails

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import com.test.cryptoapp.R
import com.test.cryptoapp.data.adapters.ChartPointsToListEntryAdapter
import com.test.cryptoapp.databinding.FragmentCoinDelailsBinding
import com.test.cryptoapp.domain.models.ChartPoints
import com.test.cryptoapp.domain.models.Coin
import com.test.cryptoapp.domain.models.UiState
import com.test.cryptoapp.ui.activities.OnFragmentInteractionListener
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentCoinDetails : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentCoinDelailsBinding
    private val coinViewModel: CoinDetailsViewModel by viewModel()
    private lateinit var currentText: TextView
    private var marketCapCoin: Float = 0.0F
    private var currentPriceCoin: Float = 0.0F
    private lateinit var idCoin: String
    private var percentage: String = "24H"
    private lateinit var icon: String
    private lateinit var name: String
    private var days: String = "1"
    private lateinit var chart: LineChart


    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 15000
        }
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoinDelailsBinding.inflate(layoutInflater)
        idCoin = arguments?.getString("id")!!
        marketCapCoin = arguments?.getFloat("marketCap")!!
        percentage = arguments?.getString("percentage")!!
        currentPriceCoin = arguments?.getFloat("currentPrice")!!
        icon = arguments?.getString("icon")!!
        name = arguments?.getString("name")!!
        with(binding){
            toolbar.inflateMenu(R.menu.coindelails_menu)
            day1.setOnClickListener(this@FragmentCoinDetails)
            days7.setOnClickListener(this@FragmentCoinDetails)
            days30.setOnClickListener(this@FragmentCoinDetails)
            days365.setOnClickListener(this@FragmentCoinDetails)
            all.setOnClickListener(this@FragmentCoinDetails)
            toolbar.title = "  $name"
            toolbar.setNavigationIcon(R.drawable.back_from_details_fragment)
            toolbar.setNavigationOnClickListener {
                findNavController().navigate(
                    R.id.action_fragmentCoinDetails_to_fragmentCoinsList
                )
            }
        }

        currentText = binding.day1

        setLogo()

        var coin = Coin(
            cryptoId = idCoin,
            currentPriceCoin = currentPriceCoin.toDouble(),
            cryptoName = null,
            cryptoSymbol = null,
            urlItemCrypto = null,
            changePercentage = percentage,
            marketCap = marketCapCoin.toDouble()
        )

        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }
        sharedElementEnterTransition =
            TransitionInflater.from(this.context).inflateTransition(android.R.transition.move)
        if (mListener != null) {
            mListener!!.onFragmentInteraction(idCoin)
        }

        chart = binding.livechart
        initChart()
        setupViewModel()
        showDataCoin(coin)
        setHasOptionsMenu(true)
        coinViewModel.requestsForCoinDetails(idCoin, days, percentage)
        return binding.root
    }

    private fun initChart() {
        with(chart) {
            description.isEnabled = false

            setTouchEnabled(true)
            isDragEnabled = false
            setScaleEnabled(false)
            setDrawGridBackground(false)
            setDrawBorders(false)
            description.isEnabled = false
            legend.isEnabled = false

            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawLabels(false)
            axisLeft.setDrawAxisLine(false)

            xAxis.setDrawGridLines(false)
            xAxis.setDrawLabels(false)
            xAxis.setDrawAxisLine(false)

            axisRight.setDrawGridLines(false)
            axisRight.setDrawLabels(false)
            axisRight.setDrawAxisLine(false)

            setBackgroundColor(Color.WHITE)
        }
    }

    private fun drawChart(chartPoints: ChartPoints) {

        val listEntry = ChartPointsToListEntryAdapter().convert(chartPoints)
        val set1 = LineDataSet(listEntry, "chart")
        with(set1) {
            axisDependency = AxisDependency.LEFT
            color = ContextCompat.getColor(requireContext(), R.color.deep_saffron)
            setDrawValues(false)
            valueTextColor = ContextCompat.getColor(requireContext(), R.color.deep_saffron)
            lineWidth = 1.5f
            setDrawCircles(false)
            setDrawValues(false)
            fillAlpha = 0
            fillColor = ColorTemplate.getHoloBlue()
            highLightColor = Color.rgb(244, 117, 117)
            setDrawCircleHole(false)
            disableDashedHighlightLine()
            disableDashedLine()
            setDrawHighlightIndicators(false)
        }

        val data = LineData(set1)
        with(data) {
            setValueTextColor(Color.WHITE)
            setValueTextSize(9f)
        }

        chart.data = data
        val min = listEntry.minByOrNull { entry -> entry.y }
        val max = listEntry.maxByOrNull { entry -> entry.y }
        with(binding) {
            lowPrice.text = min!!.y.toString()
            highPrice.text = max!!.y.toString()
        }

        chart.invalidate()
    }

    private fun showDataCoin(coin: Coin) {
        with(binding){
            priceText.text = String.format("%.2f$", coin.currentPriceCoin)
            marketPricePercentage.text = coin.changePercentage + " %"
            marketCap.text = "$ " + String.format("%.2f", coin.marketCap)
        }
    }

    private fun setupViewModel() {
        lifecycleScope.launchWhenStarted {
            coinViewModel.myUiState.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> changeProgressBarVisibility(true)
                    is UiState.Success<Pair<Coin, ChartPoints>> -> {

                        changeProgressBarVisibility(false)
                        showDataCoin(uiState.data.first)
                        drawChart(uiState.data.second)
                    }
                    is UiState.Error -> {
                        showErrorMessage(uiState.error)
                        changeProgressBarVisibility(false)
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        val newTextView = when (v?.id) {
            R.id.day1 -> {
                days = resources.getString(R.string.point_1day)
                percentage = resources.getString(R.string.point_chart_24H)
                binding.day1
            }
            R.id.days7 -> {
                days = resources.getString(R.string.point_7days)
                percentage = resources.getString(R.string.point_chart_7d)
                binding.days7
            }
            R.id.days30 -> {
                days = resources.getString(R.string.point_30days)
                percentage = resources.getString(R.string.point_chart_30d)
                binding.days30
            }
            R.id.days365 -> {
                days = resources.getString(R.string.point_365days)
                percentage = resources.getString(R.string.point_chart_200d)
                binding.days365
            }
            R.id.all -> {
                days = resources.getString(R.string.point_ALL)
                binding.all
            }
            else -> days
        }
        (newTextView as? TextView)?.let { changeTextView(currentText, it) }
        coinViewModel.requestsForCoinDetails(idCoin, days, percentage)
    }

    private fun changeTextView(currentTextView: TextView, newText: TextView) {
        with(currentTextView){
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.silver_sand))
        }
        with(newText){
            background = ContextCompat.getDrawable(requireContext(), R.drawable.background_time_chart)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        currentText = newText
    }

    private fun showErrorMessage(exception: String) {
        view?.let {
            val snack = Snackbar.make(it, exception, Snackbar.LENGTH_LONG)
            snack.show()
        }
    }

    private fun changeProgressBarVisibility(show: Boolean) {
        binding.progressBar.isVisible = show
    }

    private fun setLogo() {
        Glide.with(binding.toolbar.context)
            .asBitmap()
            .load(icon)
            //.override(R.dimen.logo_width, R.dimen.logo_height)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    var resourceChangedSize = getResizedBitmap(
                        resource,
                        resources.getDimension(R.dimen.logo_width).toInt(),
                        resources.getDimension(R.dimen.logo_height).toInt()
                    )
                    val drawable = BitmapDrawable(resources, resourceChangedSize)
                    binding.toolbar.logo = drawable
                }

            })
    }

    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)
        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }

}