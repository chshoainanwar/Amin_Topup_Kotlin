package com.example.user.contactlist.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.user.contactlist.databinding.FragmentContactListBinding
import com.example.user.contactlist.model.Contact
import com.example.user.contactlist.viewmodel.ContactViewModel
import me.zhouzhuo.zzletterssidebar.interf.OnLetterTouchListener


open class ContactListFragment : Fragment() {
    private var contactViewModel: ContactViewModel? = null
    private var binding: FragmentContactListBinding? = null
    private val _binding: FragmentContactListBinding
        get() {
            return binding!!
        }

    var adapter: ContactAdapter? = null

    @Suppress("SameParameterValue")
    private fun hasPhoneContactsPermission(permission: String): Boolean {
        val hasPermission = ContextCompat.checkSelfPermission(requireActivity(), permission)
        return hasPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        contactViewModel = ContactViewModel(requireContext().applicationContext)
        val bind = FragmentContactListBinding.inflate(
            inflater,
            container,
            false
        )
        binding = bind
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasPhoneContactsPermission(Manifest.permission.READ_CONTACTS)) {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        } else {
            initRecyclerView()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // PERMISSION GRANTED
            initRecyclerView()
        } else {
            // PERMISSION NOT GRANTED
            Toast.makeText(requireContext(), "Contact Permission Required", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
            intent.data = uri
            requireActivity().onBackPressed()
            startActivity(intent)
        }
    }

    private var mData: ArrayList<Contact> = ArrayList()
    private var mDataDummy: ArrayList<Contact> = ArrayList()
    private var tvDialog: TextView? = null
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun initRecyclerView() {
        mData.clear()
        mDataDummy.clear()
        mData.addAll(contactViewModel?.contacts ?: ArrayList())
        mDataDummy.addAll(contactViewModel?.contacts ?: ArrayList())
        tvDialog = _binding.tvDialog
        adapter = ContactAdapter(
            _binding.contactRecyclerView.context,
            mData
        )
        adapter?.setRecyclerViewClickListener { _: View?, pos: Int ->
            callBack?.invoke(mData[pos])

        }
        _binding.contactRecyclerView.adapter = adapter
        _binding.sidebar.setLetterTouchListener(
            _binding.contactRecyclerView,
            adapter,
            tvDialog,
            object : OnLetterTouchListener {
                override fun onLetterTouch(letter: String, position: Int) {}
                override fun onActionUp() {}
            })
        _binding.etSearchContact.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                adapter?.getFilter(mDataDummy)?.filter(s)
            }
        })

    }


    var callBack: ((Contact) -> Unit)? = null

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ContactListFragment.
         */
        fun newInstance(callBackParam: ((Contact) -> Unit)? = null): ContactListFragment {
            val itemFra = ContactListFragment()
            itemFra.callBack = callBackParam

            return itemFra
        }
    }
}