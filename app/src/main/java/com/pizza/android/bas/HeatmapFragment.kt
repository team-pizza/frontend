package com.pizza.android.bas

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.pizza.android.bas.networking.GroupList
import android.widget.TextView

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HeatmapFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class HeatmapFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var reponseGroup: GroupList? = null
    private var requestGroup: GroupList = GroupList("Test", "", listOf(""))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_heatmap, container, false)
        view.findViewById<Button>(R.id.button2).setOnClickListener {
            findNavController().navigate(R.id.action_heatmapFragment_to_groupListFragment)
                (this.activity as MainActivity).postToBackend<GroupList, GroupList>("/sendGroup/upload", requestGroup) {
                    if(it!=null) {
                      // Success
                        this.reponseGroup = it
                        displayGroup()
                    }
                }
        }
        return view
    }

    // Displays group information from call
    private fun displayGroup(){
        // Displays group name
        val groupTitle: TextView = R.id.HeatGroupTitle as TextView
        groupTitle.text = this.reponseGroup?.groupName

        // Displays first two group members; If possible, should be switched to recycler view format upon getting a replacement
        val groupMember1: TextView = R.id.TeamMember1 as TextView
        groupMember1.text = this.reponseGroup?.groupMembers?.get(0)
        val groupMember2: TextView = R.id.TeamMember2 as TextView
        groupMember2.text = this.reponseGroup?.groupMembers?.get(1)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun navGroupList(view: View) {
        // Do something in response to button click
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
}
