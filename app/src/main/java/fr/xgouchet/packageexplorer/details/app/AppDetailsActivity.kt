package fr.xgouchet.packageexplorer.details.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import fr.xgouchet.packageexplorer.applist.AppViewModel
import fr.xgouchet.packageexplorer.details.AppInfoViewModel
import fr.xgouchet.packageexplorer.ui.mvp.BaseActivity

/**
 * @author Xavier F. Gouchet
 */
class AppDetailsActivity
    : BaseActivity<AppViewModel, List<AppInfoViewModel>, AppDetailsPresenter, AppDetailsFragment>() {

    companion object {

        val EXTRA_PACKAGE_NAME = "package_name"

        fun startWithApp(activity: Activity, app: AppViewModel) {
            val intent = Intent(activity, AppDetailsActivity::class.java)
            intent.putExtra(EXTRA_PACKAGE_NAME, app.packageName)
            activity.startActivity(intent)
        }

    }

    override val allowNullIntentData: Boolean = false

    override fun readIntent(intent: Intent): AppViewModel? {
        val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME)
        if (packageName.isNullOrBlank()) {
            return null
        }

        return AppViewModel.fromPackageName(this, packageName)
    }

    override fun instantiatePresenter(): AppDetailsPresenter {
        val appViewModel = intentData ?: throw IllegalStateException("Expected non null app here")
        return AppDetailsPresenter(this, appViewModel.packageName, appViewModel.isSystemApp)
    }

    override fun instantiateFragment(): AppDetailsFragment {
        return AppDetailsFragment()
    }

    override fun getPresenterKey(): String {
        val appViewModel = intentData ?: throw IllegalStateException("Expected non null app here")
        return "app_details/${appViewModel.packageName}"
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        intentData?.let {
            title = it.title
            toolbar.subtitle = it.packageName
        }
    }

}