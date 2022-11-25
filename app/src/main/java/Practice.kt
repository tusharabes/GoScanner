import android.os.Looper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.logging.Handler

fun main() {

    GlobalScope.launch(Dispatchers.IO) {
        print("Hello Guys")
    }


}