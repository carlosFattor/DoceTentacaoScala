package modules

import com.google.inject.AbstractModule
import models.daos.GalleryDAO
import models.daos.GalleryDAOImp
import models.services.GalleryService
import models.services.GalleryServiceImp
import reactivemongo.api.DB
import reactivemongo.api.MongoConnectionOptions
import reactivemongo.api.MongoDriver

/**
 * @author carlos
 */
class DIModule extends AbstractModule {
  
  def configure() = {
    bind(classOf[DB]).toInstance{
       import com.typesafe.config.ConfigFactory
      import scala.concurrent.ExecutionContext.Implicits.global
      import scala.collection.JavaConversions._  
      
      lazy val config = ConfigFactory.load
      lazy val driver = new MongoDriver
      lazy val connection = driver.connection(
        config.getStringList("mongodb.servers"),
        MongoConnectionOptions(),
        Seq()
        )
        connection.db(config.getString("mongodb.db"))
    }
    bind(classOf[GalleryDAO]).to(classOf[GalleryDAOImp])
    bind(classOf[GalleryService]).to(classOf[GalleryServiceImp])
  }
  
}