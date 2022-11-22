import com.woory.data.source.FirebaseDataSource
import com.woory.network.DefaultFirebaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseDataSource(): FirebaseDataSource = DefaultFirebaseDataSource()
}