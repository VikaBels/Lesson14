package com.example.lesson_14.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson_14.databinding.ListElementBinding
import com.example.lesson_14.interfaces.DeleteElementListener
import com.example.lesson_14.models.Element

class ElementAdapter(
    private val context: Context,
    private val elementList: ArrayList<Element>,
    private val listenerForDeleteElement: DeleteElementListener?,
) : RecyclerView.Adapter<ElementAdapter.ElementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementViewHolder {
        val binding = ListElementBinding.inflate(LayoutInflater.from(context), parent, false)
        return ElementViewHolder(binding, listenerForDeleteElement)
    }

    override fun onBindViewHolder(holder: ElementViewHolder, position: Int) {
        val elementItem = elementList[position]
        holder.bind(elementItem)
    }

    override fun getItemCount(): Int {
        return elementList.size
    }

    class ElementViewHolder(
        private val binding: ListElementBinding,
        private val listenerForFragment: DeleteElementListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        private var previousTextWatcher: TextWatcher? = null

        fun bind(elementItem: Element) {
            val currentTextWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    elementItem.safeName = s.toString()
                }
            }

            previousTextWatcher = if (previousTextWatcher == null) {
                currentTextWatcher
            } else {
                binding.itemTitle.removeTextChangedListener(previousTextWatcher)
                currentTextWatcher
            }

            binding.itemTitle.setText(elementItem.safeName)
            binding.txtNumber.text = elementItem.safeQuantity.toString()

            binding.itemTitle.addTextChangedListener(currentTextWatcher)

            binding.btnBlackMinus.setOnClickListener {
                listenerForFragment?.deleteElement(elementItem)
            }

            binding.btnMinus.setOnClickListener {
                elementItem.safeQuantity--
                binding.txtNumber.text = elementItem.safeQuantity.toString()
            }

            binding.btnPlus.setOnClickListener {
                elementItem.safeQuantity++
                binding.txtNumber.text = elementItem.safeQuantity.toString()
            }
        }
    }
}