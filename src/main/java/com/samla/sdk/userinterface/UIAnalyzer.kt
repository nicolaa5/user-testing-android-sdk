package com.samla.sdk.userinterface

import android.util.Log
import android.util.Pair
import android.view.View
import android.view.ViewGroup
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
            val screenshot = UIHierarchy.takeScreenshot(view)
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



    fun setViewLayoutChangedListener (view: View, recurse : Boolean, callback :(View) -> Unit) {
        if (recurse)
            depthFirstSearch (view) {foundView ->
                foundView.addOnLayoutChangeListener { view, p1, p2, p3, p4, p5, p6, p7, p8 ->
                    Log.i(TAG, "onLayoutChange: " + view.javaClass.simpleName)
                    callback.invoke(view)
                }
            }
        else
            view.addOnLayoutChangeListener { view, p1, p2, p3, p4, p5, p6, p7, p8 ->
                Log.i(TAG, "onLayoutChange: " + view.javaClass.simpleName)
                callback.invoke(view)
            }
    }

    fun setViewChildChangedListener ( viewGroup : ViewGroup, callback :(View) -> Unit) {
        viewGroup.setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {

            override fun onChildViewRemoved(parent: View, child: View) {
                Log.i(TAG, "onChildViewRemoved")
            }

            override fun onChildViewAdded(parent: View, child: View) {
                Log.i(TAG, "onChildViewAdded")
                callback.invoke(child)
            }
        })
    }

    fun setInteractableElementListeners (view: View, recurse : Boolean,  callback :(View) -> Unit ) {
        if (recurse)
            depthFirstSearch(view) { foundView ->
                Log.i(TAG, "view: " + view.javaClass.simpleName)

                if (foundView.isClickable) {
                    foundView.setOnClickListener {
                        callback.invoke(foundView)
                    }
                }
            }
        else
            if (view.isClickable) {
                view.setOnClickListener {
                    callback.invoke(view)
                }
            }
    }

    fun setViewVisibilityListener (view: View, recurse : Boolean, callback :(View) -> Unit ) {
        if (recurse)
            depthFirstSearch (view) {foundView ->
                //Based on Visibility.GONE, Visibility.VISIBLE, Visibility.INVISIBLE
                foundView.setOnSystemUiVisibilityChangeListener { visibility ->
                    when (visibility) {
                        View.VISIBLE ->
                            Log.i(TAG, "onSystemUiVisibilityChanged: " + foundView + " VISIBLE" )
                        View.INVISIBLE ->
                            Log.i(TAG, "onSystemUiVisibilityChanged: " + foundView + " INVISIBLE" )
                        View.GONE ->
                            Log.i(TAG, "onSystemUiVisibilityChanged: " + foundView + " GONE" )
                    }
                }
            }
        else
            view.setOnSystemUiVisibilityChangeListener { visibility ->
                when (visibility) {
                    View.VISIBLE ->
                        Log.i(TAG, "onSystemUiVisibilityChanged: " + view + " VISIBLE" )
                    View.INVISIBLE ->
                        Log.i(TAG, "onSystemUiVisibilityChanged: " + view + " INVISIBLE" )
                    View.GONE ->
                        Log.i(TAG, "onSystemUiVisibilityChanged: " + view + " GONE" )
                }
            }
    }

    fun depthFirstSearch (view : View, callback :(View) -> Unit ) {
        val hierarchy = Stack<Pair<String, View>>()
        hierarchy.push(Pair.create("", view))

        while (!hierarchy.empty()) {
            val pair = hierarchy.pop()
            val view = pair.second
            callback.invoke(view)

            when (view) {
                is ViewGroup -> {
                    val vg = view
                    for (i in (vg.childCount - 1) downTo 0) {
                        hierarchy.push(
                            Pair.create(
                                pair.first.toString(),
                                vg.getChildAt(i)
                            )
                        )
                    }
                }
            }
        }
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
