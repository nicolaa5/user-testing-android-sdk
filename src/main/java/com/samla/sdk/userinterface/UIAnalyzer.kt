package com.samla.sdk.userinterface

import android.util.Log
import android.util.Pair
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import com.samla.sdk.ISamla
import com.samla.sdk.R
import com.samla.sdk.database.DatabaseFactory
import com.samla.sdk.storage.DataStorage
import retrofit2.http.GET
import java.util.*

object UIAnalyzer {
    private val TAG = UIAnalyzer::class.java.simpleName

    lateinit var ISamla: ISamla

    fun setClientActivity(entry : ISamla) {
        ISamla = entry;
    }

    /**
     * Sets the screen that will be build as part of the userflow
     * Takes a screenshot of the current screen after the UI layout is finished rendering
     *
     */
    public fun createScreen(view : View) {

    }

    /**
     * Setting of the tags can be done if we let the userflow be created by a non-developer.
     *
     * - Let client tap the screens/actions to add to the userflow (in a unique build?)
     * - The client SDK add tags to the tapped screens/views
     * - All end user SDK pull the new userflow screens/views and attach listeners
     */
    public fun createAction(view : View, actionSource : Int, actionDestination : Int) {

        val action = Action(
            view,
            actionSource,
            actionDestination
        )
        view.setTag(R.id.action, action)
    }

    public fun setScreen (view : View) {
        val screen = Screen(view);
        view.setTag(R.id.screen, screen)

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val screenshot = UIHierarchy.getScreenshot(view)
            val storedScreenshot = UIHierarchy.storeScreenshot(screenshot, ISamla.getActivity(), 100)
            UIHierarchy.openScreenshot(ISamla.getActivity(), storedScreenshot)
        }
    }

    fun setAction (view : View) {
        view.viewTreeObserver.addOnGlobalLayoutListener {
            view.setOnClickListener { clickedView ->
                clickedView.x;
                clickedView.y;
                clickedView.width;
                clickedView.height;

                DataStorage.storeAction(clickedView, "11851958");
            }
        }
    }

    fun setUIHierarchyChangeListener (viewGroup : ViewGroup, callback :(View) -> Unit) {
        viewGroup.setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewRemoved(parent: View, child: View) {
                callback.invoke(child)
            }

            override fun onChildViewAdded(parent: View, child: View) {
                callback.invoke(child)
            }

        })
        viewGroup.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                view: View,
                p1: Int,
                p2: Int,
                p3: Int,
                p4: Int,
                p5: Int,
                p6: Int,
                p7: Int,
                p8: Int
            ) {
                callback.invoke(view)
            }

        })
    }

    fun setInteractableElementListeners (hierarchy: Stack<Pair<String, View>> ) {
        val buttonList : MutableList<Button> = mutableListOf()
        val menuList : MutableList<Menu> = mutableListOf()
        val listList : MutableList<ListView> = mutableListOf()

        while (!hierarchy.empty()) {
            val pair = hierarchy.pop()
            val view = pair.second

            view.setOnClickListener { Log.i(TAG, "View: " + view.javaClass.simpleName) }

            if (view is ViewGroup) {
                val vg = view
                for (i in vg.childCount - 1 downTo 0) {
                    hierarchy.push(
                        Pair.create(
                            pair.first.toString(),
                            vg.getChildAt(i)
                        )
                    )
                }
            }
        }

        for (pair :Pair<String, View> in hierarchy) {
            val view : View = pair.second;

            when (view) {
                is Button -> {
                    buttonList.add(view)
                }
                is Menu -> menuList.add(view)
                is ListView -> listList.add(view)
                else -> {

                }
            }
        }
//        getUserflowBuildVersionFromServer("apiKey"){ userFlowBuild ->
//            println()
//        }
    }


    @GET ("samla.com/dev/{user}/userflow/build")
    fun getUserflowBuildVersionFromServer (apiKey : String, callback :(String) -> Unit) {
        DatabaseFactory.getDatabaseAccess(ISamla.getActivity())
    }

    /**
     * Retrieves the stored UI components that are part of the Userflow
     */
    @GET ("samla.com/dev/{user}/userflow")
    fun getUserflowIdentifiersFromServer () : Stack<Pair<String, View>>? {
        return null;
    }

    /**
     * Sets the stored UI components that are part of the Userflow
     */
    fun setUserflowIdentifiersFromStorage () {

    }

    /**
     * Retrieves the stored UI components that are part of the Userflow
     */
    fun getUserflowIdentifiersFromStorage () : Stack<Pair<String, View>>? {
        return null;
    }

}

public class Screen(screenView : View) {
    val view : View = screenView;
}

public class Action(actionView : View, actionSource : Int, actionDestination : Int) {
    val view : View = actionView;
    val source : Int =  actionSource;
    val destination : Int =  actionDestination;
}
