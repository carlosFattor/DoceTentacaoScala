package modules

import scala.concurrent.ExecutionContext.Implicits
import com.google.inject.AbstractModule
import models.daos.GalleryDAOImp
import models.daos.UserDAOImp
import models.daos.GalleryDAO
import models.daos.UserDAO
import models.services.GalleryServiceImp
import models.services.GalleryService
import play.api.Configuration
import play.api.Environment
import reactivemongo.api.DB
import reactivemongo.api.MongoDriver
import models.services.traits.UserService
import models.services.UserServiceImp

/**
 * @author carlos
 */

case class DIModule(environment: Environment, configuration: Configuration) extends AbstractModule {
  import scala.concurrent.ExecutionContext.Implicits.global

  def configure() {
    bindMongo()
    bindDAOs()
    bindServices()
  }
  
  private def bindMongo(): Unit = {
    val hosts = configuration.getStringSeq("mongodb.server").get.asInstanceOf[List[String]]
    val port = configuration.getInt("mongodb.port").get
    val dbName = configuration.getString("mongodb.db").get

    val driver = new MongoDriver
    val connection = driver.connection(hosts)
    val db: DB = connection(dbName)
    bind(classOf[DB]).toInstance(db)
  }
  
  private def bindDAOs(): Unit = {
    bind(classOf[GalleryDAO]).to(classOf[GalleryDAOImp])
    bind(classOf[UserDAO]).to(classOf[UserDAOImp])
    
  }
  
  private def bindServices(): Unit = {
    bind(classOf[GalleryService]).to(classOf[GalleryServiceImp])
    bind(classOf[UserService]).to(classOf[UserServiceImp])
  }
  
}
