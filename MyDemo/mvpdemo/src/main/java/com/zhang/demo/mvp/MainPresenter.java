package com.zhang.demo.mvp;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/1.
 */
public class MainPresenter {
    private MainView mainView;
    private TaskManager taskData;

    public MainPresenter() {
        this.taskData = new TaskManager(new TaskDataSourceImpl());
    }

    public MainPresenter test() {
        this.taskData = new TaskManager(new TaskDataSourceTestImpl());
        return this;
    }

    public MainPresenter addTaskListener(MainView viewListener) {
        this.mainView = viewListener;
        return this;
    }

    public void getString() {
//        String str = taskData.getShowContent();
//        mainView.onShowString(str);

        Func1 dataAction = new Func1<String, String>() {
            @Override
            public String call(String param) {
                return taskData.getShowContent();
            }
        };
        Action1 viewAction = new Action1<String>() {
            @Override
            public void call(String str) {
                mainView.onShowString(str);
            }
        };
        Observable.just("")
                .observeOn(Schedulers.io())
                .map(dataAction)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewAction);

    }


}
