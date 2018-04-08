package dave.gymschedule

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CheckBox
import android.widget.TextView
import dave.gymschedule.interactor.EventTypeStateInteractor
import dave.gymschedule.model.EventType
import javax.inject.Inject

class GymScheduleExpandableListAdapter(private val context: Context,
                                       private val eventType: EventType,
                                       private val children: List<String>) : BaseExpandableListAdapter() {

    companion object {
        private val TAG =  GymScheduleExpandableListAdapter::class.java.simpleName
    }

    @Inject
    lateinit var eventTypeStateInteractor: EventTypeStateInteractor

    init {
        GymScheduleApplication.graph.inject(this)
    }

    override fun getGroup(groupPosition: Int): Any {
        return eventType
    }

    override fun getGroupCount(): Int {
        return 1
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var _convertView = convertView
        if (_convertView == null) {
            _convertView = LayoutInflater.from(context).inflate(R.layout.list_header, null)
        }

        val headerCheckbox = _convertView!!.findViewById<CheckBox>(R.id.list_header_checkbox)
        eventTypeStateInteractor.getEventTypeMapPublishSubject()
                .subscribe {
                    Log.d(TAG, "received event state list, updating checkbox")
                    headerCheckbox.isChecked = it.getOrDefault(eventType.eventTypeId, false)
                }
        headerCheckbox.setOnClickListener { _ ->
            Log.d(TAG, "updating event type state")
            eventTypeStateInteractor.updateEventTypeState(eventType, headerCheckbox.isChecked)
        }
        _convertView.findViewById<TextView>(R.id.list_header_text).text = eventType.eventName

        return _convertView
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return children[childPosition]
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return children.size
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean,
                              convertView: View?, parent: ViewGroup?): View {
        var _convertView = convertView
        if (_convertView == null) {
            _convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null)
        }

        _convertView!!.findViewById<CheckBox>(R.id.list_item_checkbox).isChecked = false
        _convertView.findViewById<TextView>(R.id.list_item_text).text = children[childPosition]

        return _convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

}