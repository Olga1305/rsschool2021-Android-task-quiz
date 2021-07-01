package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rsschool.quiz.databinding.FragmentResultBinding

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private var listener: QuizInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val result = arguments?.getInt(RESULT_KEY)
        "Your result: ${result.toString()} from 50".also { binding.result.text = it }

        binding.share.setOnClickListener {
            listener?.share(result)
        }

        binding.restart.setOnClickListener {
            listener?.restart()
        }

        binding.close.setOnClickListener {
            listener?.close()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? QuizInterface
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    companion object {

        @JvmStatic
        fun newInstance(result: Int): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putInt(RESULT_KEY, result)
            fragment.arguments = args
            return fragment
        }
        private const val RESULT_KEY = "RESULT"
    }
}