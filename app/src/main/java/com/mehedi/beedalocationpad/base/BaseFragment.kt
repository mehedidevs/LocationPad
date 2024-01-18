package com.mehedi.beedalocationpad.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    protected var _binding: B? = null
    val binding get() = _binding!!

    protected lateinit var mContext: Context
    lateinit var toast: Toast

    protected val TAG by lazy {
        "#X_${parentFragmentManager.fragments.last()::class.simpleName}"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toast = Toast.makeText(mContext, null, Toast.LENGTH_LONG)

        init(savedInstanceState)


    }

    protected abstract fun init(savedInstanceState: Bundle?)


    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(mContext)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getBinding(inflater, container)
        return binding.root
    }

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): B


    fun showToast(msg: String) {
        toast.apply {
            toast.setText(msg)
            show()
        }
    }


    override fun onDetach() {
        _binding = null
        super.onDetach()
    }


}