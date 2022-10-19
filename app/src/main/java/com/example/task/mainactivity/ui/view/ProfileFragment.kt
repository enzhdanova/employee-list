package com.example.task.mainactivity.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.task.mainactivity.R
import com.example.task.mainactivity.data.model.Employee
import com.example.task.mainactivity.databinding.FragmentProfileBinding
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle ->
            val birthdate = bundle.getString(ARG_DATE)
            val ageInString = Period.between(LocalDate.parse(birthdate), LocalDate.now()).years
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

            with(binding) {
                name.text = bundle.getString(ARG_NAME)
                nickname.text = bundle.getString(ARG_NICKNAME)
                phone.text = bundle.getString(ARG_PHONE)
                position.text = bundle.getString(ARG_POSITION)
                birthday.text = LocalDate.parse(birthdate).format(formatter)
                age.text = resources.getQuantityString(R.plurals.plular_age, ageInString, age)

                avatar.let { it ->
                    Glide
                        .with(view)
                        .load(bundle.getString(ARG_PHOTO))
                        .into(it)
                }

                back.setOnClickListener(listenerBack)
                phone.setOnClickListener(listenerDialPhoneNumber)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private val listenerBack = View.OnClickListener {
        activity?.supportFragmentManager?.commit {
            remove(this@ProfileFragment)
        }
    }

    private val listenerDialPhoneNumber = View.OnClickListener {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${binding.phone.text}")
        }
        startActivity(intent)
    }

    companion object {
        val TAG: String = ProfileFragment::class.java.simpleName

        private const val ARG_NAME = "param_name"
        private const val ARG_DATE = "param_date"
        private const val ARG_PHONE = "param_phone"
        private const val ARG_NICKNAME = "param_nickname"
        private const val ARG_POSITION = "param_position"
        private const val ARG_PHOTO = "param_photo"

        fun newInstance(employee: Employee) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    val fullName = employee.firstName + " " + employee.lastName
                    putString(ARG_NAME, fullName)
                    putString(ARG_NICKNAME, employee.userTag)
                    putString(ARG_PHONE, employee.phone)
                    putString(ARG_DATE, employee.birthday.toString())
                    putString(ARG_POSITION, employee.position)
                    putString(ARG_PHOTO, employee.avatarUrl)
                }
            }
    }
}