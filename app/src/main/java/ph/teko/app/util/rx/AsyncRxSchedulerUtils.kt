package ph.teko.app.util.rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.CompletableTransformer
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

internal class AsyncRxSchedulerUtils @Inject constructor() : RxSchedulerUtils {

    override fun completableAsyncSchedulerTransformer(): CompletableTransformer =
        CompletableTransformer { upstream ->
            upstream.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
        }

    override fun <T> observableAsyncSchedulerTransformer(): ObservableTransformer<T, T> =
        ObservableTransformer { upstream ->
            upstream.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
        }

    override fun <T> singleAsyncSchedulerTransformer(): SingleTransformer<T, T> =
        SingleTransformer { upstream ->
            upstream.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
        }
}
