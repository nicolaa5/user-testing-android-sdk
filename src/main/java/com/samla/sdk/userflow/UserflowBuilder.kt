package com.samla.sdk.userflow

import android.util.Pair
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ListView
import com.samla.sdk.ISamla
import com.samla.sdk.R
import com.samla.sdk.storage.DataStorage
import com.samla.sdk.userinterface.UserInterfaceHierarchy
import java.util.*

object UserflowBuilder {
    private val TAG = UserflowBuilder::class.java.simpleName

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

        val action = Action (view, actionSource, actionDestination)
        view.setTag(R.id.action, action)
    }

    public fun setScreen (view : View) {
        val screen = Screen(view);
        view.setTag(R.id.screen, screen)

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val screenshot = UserInterfaceHierarchy.getScreenshot(view)
            val storedScreenshot = UserInterfaceHierarchy.storeScreenshot(screenshot, ISamla.getActivity(), 100)
            UserInterfaceHierarchy.openScreenshot(ISamla.getActivity(), storedScreenshot)
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

    fun buildUserFlow () {
        val buttonList : MutableList<Button> = mutableListOf()
        val menuList : MutableList<Menu> = mutableListOf()
        val listList : MutableList<ListView> = mutableListOf()
        val hierarchy : Stack<Pair<String, View>> =
            UserInterfaceHierarchy.getViewHierarchy(
                ISamla.getActivity().findViewById<View>(android.R.id.content)
            )

        for (pair :Pair<String, View> in hierarchy) {
            val view : View = pair.second;

            when (view) {
                is Button -> buttonList.add(view)
                is Menu -> menuList.add(view)
                is ListView -> listList.add(view)
                else -> {

                }
            }
        }
    }

}

public class Screen(screenView : View) {
    var view : View = screenView;
}

public class Action(actionView : View, actionSource : Int, actionDestination : Int) {
    var view : View = actionView;
    var source : Int =  actionSource;
    var destination : Int =  actionDestination;
}
