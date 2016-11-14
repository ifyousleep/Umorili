package com.ifyou.umorili.fragments

import android.app.Dialog
import android.content.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.Spanned
import android.view.View
import com.ifyou.umorili.App
import com.ifyou.umorili.R
import com.ifyou.umorili.adapters.MainAdapter
import com.ifyou.umorili.net.PostModel
import com.ifyou.umorili.utils.ClickListener
import com.ifyou.umorili.utils.RecyclerTouchListener
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.retrofit_error.view.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.support.v4.withArguments
import rx.Observable

class MainFragment : RxRetrofitFragment() {

    private var rv: LinearLayoutManager? = null

    companion object {
        fun newInstance(site: String = "", name: String = "", num: Int = 20): MainFragment {
            return MainFragment().withArguments("site" to site, "name" to name, "num" to num)
        }
    }

    fun scrollTop() {
        rv?.scrollToPosition(0)
    }

    override fun onGetSite(): String {
        return arguments.getString("name")
    }

    override fun onGetObservable(): Observable<List<PostModel>> {
        return App.API().getPosts(site = arguments.getString("site"),
                name = arguments.getString("name"), num = arguments.getInt("num"))
    }

    override fun onCreateErrorView(): View {
        val view = inflate(R.layout.retrofit_error)
        view.textError.text = getString(R.string.error)
        view.timeline_swipe_layout.setColorSchemeResources(R.color.colorAccent)
        view.timeline_swipe_layout.setOnRefreshListener {
            fetchData()
        }
        return view
    }

    override fun onCreateSuccessView(data: List<Any>): View {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val textSize = sharedPreferences.getString("pref_font", "14")
        val view = inflate(R.layout.fragment_main)
        view.recyclerView.setHasFixedSize(true)
        rv = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.recyclerView.layoutManager = rv
        view.recyclerView.adapter = MainAdapter(activity, data, textSize.toFloat())
        //view.recyclerView.addItemDecoration(SimpleDividerItemDecoration(context))
        view.recyclerView.addOnItemTouchListener(RecyclerTouchListener(context, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                val bitmap = Bitmap.createBitmap(view.width, view.height,
                        Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                view.draw(canvas)
                onCreateDialog(bitmap, position, data)?.show()
            }
        }))
        view.timeline_swipe.setColorSchemeResources(R.color.colorAccent)
        view.timeline_swipe.setOnRefreshListener {
            fetchData()
        }
        return view
    }

    private fun onCreateDialog(bitmap: Bitmap, position: Int, data: List<Any>): Dialog? {
        val builder = AlertDialog.Builder(activity)
        builder.setItems(R.array.add_actions) { dialog, which -> selectList(bitmap, which, position, data) }
        return builder.create()
    }

    private fun selectList(bitmap: Bitmap, i: Int, position: Int, data: List<Any>) {
        when (i) {
            0 -> {
                val newFile = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, null, null)
                val contentUri = Uri.parse(newFile)
                if (contentUri != null) {
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.setDataAndType(contentUri, context.contentResolver.getType(contentUri))
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                    startActivity(Intent.createChooser(shareIntent, "Choose an app"))

                }
            }
            1 -> {
                val result: Spanned
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    result = Html.fromHtml((data[position] as PostModel).elementPureHtml, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    @Suppress("DEPRECATION")
                    result = Html.fromHtml((data[position] as PostModel).elementPureHtml)
                }
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("", result)
                clipboard.primaryClip = clip
                toast(getString(R.string.succes))
            }
            2 -> {
                MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, null, null)
                toast(R.string.afterSave)
            }
        }
    }
}
