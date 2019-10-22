package com.marius.personalimdb.ui.actors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.marius.personalimdb.R
import com.marius.personalimdb.adapter.ActorAdapter
import com.marius.personalimdb.helper.OpensActorDetails
import com.marius.personalimdb.ui.actors.details.ActorDetailsActivity
import kotlinx.android.synthetic.main.fragment_actors.*

class ActorsFragment : Fragment(), OpensActorDetails {
    override fun onActorClicked(actorId: Int) {
        val intent = Intent(context, ActorDetailsActivity::class.java).apply {
            putExtra("actorId", actorId)
        }
        startActivity(intent)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ActorsViewModel::class.java)
    }

    private val actorsAdapter = ActorAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_actors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpObservers()
        viewModel.initActors()
    }

    private fun setUpObservers() {
        viewModel.actorList.observe(this, Observer {
            actorsAdapter.addItems(
                it.toMutableList(), false
            )
        })
    }

    private fun setUpRecyclerView() {
        popularActorsRecycler.layoutManager = LinearLayoutManager(context)
        popularActorsRecycler.adapter = actorsAdapter
    }
}