package com.goglitter.ui.FAQ
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.goglitter.R
import com.goglitter.domain.response.FAQsLIST

class FAQAdapter(private val context: Context) :
    RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {
    private var faqs: List<FAQsLIST>? = null
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.faq_layout, parent, false)
        return FAQViewHolder(view)
    }
    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val faq= faqs!![position]
        val expanded: Boolean = faq.isExpanded
         var expandedPosition: Int = -1
        val a="</strong>"
        val b="<strong>"
        val s = "</p>"
        val r = "<p>"
        val q = "\n"
        val w = ""
        val text1: String = faq.faqHeading!!.replace(s, "").replace(q, "").replace(r, "").replace(a,"").replace(b,"")
     //   val text2: String = faq.faqDescription!!.replace(s, "").replace(q, "").replace(r, "").replace(a,"").replace(b,"")

        holder.subItem!!.visibility = if (expanded) View.VISIBLE else View.GONE
        holder.itemTitle!!.text = Html.fromHtml(text1)
       // holder.subItemGenre!!.text = Html.fromHtml(faq.faqDescription)
        val html ="For Gold loans, generally the repayment structure is determined by calculating simple interest based on monthly reducing balance. Take for example,\\r\\n\\r\\n<table align=\\\"center\\\" border=\\\"1\\\" cellpadding=\\\"5\\\" cellspacing=\\\"0\\\">\\r\\n <tbody>\\r\\n  <tr>\\r\\n   <td>Loan Amount</td>\\r\\n   <td>Rs 1,00,000</td>\\r\\n  </tr>\\r\\n  <tr>\\r\\n   <td>Duration</td>\\r\\n   <td>12 months</td>\\r\\n  </tr>\\r\\n  <tr>\\r\\n   <td>Interest Charged*</td>\\r\\n   <td>10%</td>\\r\\n  </tr>\\r\\n  <tr>\\r\\n   <td>EMI</td>\\r\\n   <td>Rs 8,792</td>\\r\\n  </tr>\\r\\n  <tr>\\r\\n   <td>Final Payment</td>\\r\\n   <td>Rs 1,05,499</td>\\r\\n  </tr>\\r\\n </tbody>\\r\\n</table>"
        holder.webview!!.getSettings().setJavaScriptEnabled(true);
        holder.webview!!.loadData(faq.faqDescription!!, "text/html; charset=utf-8", "UTF-8");

        holder.itemView.setOnClickListener {
            val expanded: Boolean = faq.isExpanded
            faq.isExpanded= !expanded
            notifyItemChanged(position)

           /* val previousSelectedPosition = selectedItemPosition
            selectedItemPosition = position
            notifyItemChanged(previousSelectedPosition) // Refresh the previous selected item
            notifyItemChanged(selectedItemPosition)*/
        }


        if (expanded) {
            holder.imageExapnd!!.setImageResource(R.drawable.ic_close_up_arrow)
        } else {
            holder.imageExapnd!!.setImageResource(R.drawable.ic_close_down_arrow)
        }
}
    override fun getItemCount(): Int {
        return if (faqs == null) 0 else faqs!!.size
    }

    inner class FAQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageExapnd: ImageView? = null
        var itemTitle: TextView? = null
        var subItem: LinearLayout? = null
        var subItemGenre: TextView? = null
        var webview:WebView?=null

        init {
            initView(itemView)
        }

        private fun initView(view: View) {
            imageExapnd = view.findViewById(R.id.imageExapnd)
            itemTitle = view.findViewById(R.id.item_title)
            subItem = view.findViewById(R.id.sub_item)
            webview = view.findViewById(R.id.webview)
        }
    }

    fun updateFaqs(faqList: List<FAQsLIST>?) {
        faqs = faqList
        notifyDataSetChanged()
    }

}

