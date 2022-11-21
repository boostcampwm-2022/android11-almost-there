package com.woory.almostthere.ui.join

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.woory.almostthere.R
import com.woory.almostthere.databinding.FragmentInviteBinding
import com.woory.almostthere.ui.viewBinding

internal class InviteFragment : Fragment(R.layout.fragment_invite) {

    private val binding: FragmentInviteBinding by viewBinding(FragmentInviteBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView.text = "ㅎㅇ"
    }
}