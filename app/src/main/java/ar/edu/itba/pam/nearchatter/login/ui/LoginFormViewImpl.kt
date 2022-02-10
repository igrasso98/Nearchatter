package ar.edu.itba.pam.nearchatter.login.ui


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView.OnEditorActionListener
import ar.edu.itba.pam.nearchatter.R
import ar.edu.itba.pam.nearchatter.login.OnUsernameConfirmListener


class LoginFormViewImpl @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), LoginFormView {
    private var onUsernameConfirmListener: OnUsernameConfirmListener? = null
    private var usernameField: EditText? = null
    private var confirmButton: Button? = null

    init {
        inflate(context, R.layout.login_form, this)
        this.usernameField = findViewById(R.id.login_form_username_input)
        this.confirmButton = findViewById(R.id.login_form_confirm_button)
        confirmButton?.isEnabled = false
        confirmButton?.setBackgroundColor(resources.getColor(R.color.light_grey))
    }


    override fun bind() {
        usernameField?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.isEmpty()) {
                    confirmButton?.isEnabled = false
                    confirmButton?.setBackgroundColor(resources.getColor(R.color.light_grey))
                } else {
                    confirmButton?.isEnabled = true
                    confirmButton?.setBackgroundColor(resources.getColor(R.color.blue))
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })

        confirmButton?.setOnClickListener {
            onUsernameConfirmListener?.onConfirm(usernameField?.text.toString())
        }
    }

    override fun setOnUsernameConfirmListener(listener: OnUsernameConfirmListener) {
        this.onUsernameConfirmListener = listener
    }
}