package com.fund.likeeat.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.AddFriendListAdapter
import com.fund.likeeat.databinding.ActivityAddFriendBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.viewmodels.AddFriendViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddFriendActivity : AppCompatActivity() {

    private val addFriendViewModel: AddFriendViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityAddFriendBinding>(
            this,
            R.layout.activity_add_friend
        )

        val adapter = AddFriendListAdapter()
        binding.listFriends.adapter = adapter
        addFriendViewModel.friends.observe(this) {
            adapter.submitList(it)
        }

        binding.btnOk.setOnClickListener {

            lifecycleScope.launch {
                val isSuccess = addFriendViewModel.addFriend(adapter.checkedList)

                withContext(Dispatchers.Main) {
                    if (isSuccess) {
                        Toast.makeText(this@AddFriendActivity, "친구목록에 추가했습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AddFriendActivity, "친구추가를 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}