package com.ifyou.umorili.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.ifyou.umorili.R
import io.paperdb.Paper
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import kotlin.properties.Delegates

abstract class RxRetrofitFragment : Fragment() {

    private val BUNDLE_ID = "state"
    private var rootLayout: FrameLayout by Delegates.notNull()
    private var state: State = State.LOADING
    private var data: List<Any>? = null
    private var isDestroy: Boolean = false

    enum class State {
        LOADING, ERROR, SUCCESS
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(BUNDLE_ID, state)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootLayout = FrameLayout(activity)
        rootLayout.id = android.R.id.content
        if (savedInstanceState != null) {
            state = savedInstanceState.get(BUNDLE_ID) as State
        } else {
            if (Paper.book().exist(onGetSite())) {
                data = Paper.book().read(onGetSite())
                state = State.SUCCESS
            } else
                fetchData()
        }
        updateState(state)
        return rootLayout
    }

    protected abstract fun onGetSite(): String

    protected abstract fun onGetObservable(): Observable<*>

    protected fun fetchData() {
        @Suppress("UNCHECKED_CAST")
        (onGetObservable() as Observable<Any>)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(object : Subscriber<Any>() {
                    override fun onCompleted() {
                    }

                    override fun onNext(responseData: Any) {
                        data = responseData as List<Any>
                        state = State.SUCCESS
                        Paper.book().write(onGetSite(), data)
                        updateState(state)
                    }

                    override fun onError(e: Throwable) {
                        state = State.ERROR
                        updateState(state)
                        e.printStackTrace()
                    }
                })
    }

    protected fun updateState(state: State) {
        if (!isDestroy) {
            when (state) {
                State.LOADING -> replaceStateView(onCreateLoadingView())
                State.ERROR -> replaceStateView(onCreateErrorView())
                State.SUCCESS -> replaceStateView(onCreateSuccessView(data!!))
            }
        }
    }

    protected open fun onCreateLoadingView(): View = inflate(R.layout.retrofit_loading)
    protected open fun onCreateErrorView(): View = View(context)
    protected open fun onCreateSuccessView(data: List<Any>): View = View(context)

    protected fun inflate(id: Int): View {
        return inflate(id, rootLayout)
    }

    protected fun inflate(id: Int, container: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(id, container, false)
    }

    private fun replaceStateView(view: View) {
        if (rootLayout.childCount > 0) {
            val old = rootLayout.getChildAt(rootLayout.childCount - 1)

            val inAnim = AnimationUtils.loadAnimation(context, R.anim.in_bottom)
            val outAnim = AnimationUtils.loadAnimation(context, R.anim.out_bottom)
            outAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationRepeat(animation: Animation) {
                }

                override fun onAnimationEnd(animation: Animation) {
                    rootLayout.removeView(old)
                    rootLayout.addView(view)
                    view.startAnimation(inAnim)
                }
            })
            old.startAnimation(outAnim)
        } else {
            rootLayout.addView(view)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestroy = true
    }
}