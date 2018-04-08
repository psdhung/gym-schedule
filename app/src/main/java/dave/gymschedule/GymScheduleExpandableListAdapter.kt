package dave.gymschedule

import android.content.Context
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
        private val TAG = GymScheduleExpandableListAdapter::class.java.simpleName
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
        var updatedConvertView = convertView
        if (updatedConvertView == null) {
            updatedConvertView = LayoutInflater.from(context).inflate(R.layout.list_header, null)
        }

        val headerCheckbox = updatedConvertView!!.findViewById<CheckBox>(R.id.list_header_checkbox)
        eventTypeStateInteractor.getEventTypeMapPublishSubject()
                .subscribe {
                    headerCheckbox.isChecked = it.getOrDefault(eventType.eventTypeId, false)
                }
        headerCheckbox.setOnClickListener { _ ->
            eventTypeStateInteractor.updateEventTypeState(eventType, headerCheckbox.isChecked)
        }
        updatedConvertView.findViewById<TextView>(R.id.list_header_text).text = eventType.eventName

        return updatedConvertView
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
        var updatedConvertView = convertView
        if (updatedConvertView == null) {
            updatedConvertView = LayoutInflater.from(context).inflate(R.layout.list_item, null)
        }

        updatedConvertView!!.findViewById<CheckBox>(R.id.list_item_checkbox).isChecked = false
        updatedConvertView.findViewById<TextView>(R.id.list_item_text).text = children[childPosition]

        return updatedConvertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

}