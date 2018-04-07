package one.xingyi.pactstubber

import java.io.FileInputStream
import java.security.KeyStore


import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

case class SSLContextData(keyManagerFactoryPassword: String, keyStore: String, passphrase: String, trustStore: String, trustPassphrase: String)

case class ContextNameAndClientAuth(name: String, clientAuth: Boolean)

object SSLContextData {
  private var keyStore = "javax.net.ssl.keyStore"
  private var keyStorePass = "javax.net.ssl.keyStorePassword"
  private var truststore = "javax.net.ssl.trustStore"
  private var trustStorePassword = "javax.net.ssl.trustStorePassword"

  private def get(key: String) = {
    val result = System.getProperty(key)
    if (result == null) throw new NullPointerException(s"System property [$key] is null")
    result
  }

  def fromSystemProperties(): SSLContextData = SSLContextData(get(keyStorePass), get(keyStore), get(keyStorePass), get(truststore), get(trustStorePassword))

}

trait SSLContextDataToSslContext extends (SSLContextData => SSLContext)

object SSLContextDataToSslContext {

  def getSslContext(data: SSLContextData): SSLContext = {
    import data._
    val ksKeys: KeyStore = KeyStore.getInstance("JKS")
    val ksTrust = KeyStore.getInstance("JKS")
    def load(keyStore: KeyStore, store: String, password: String) = try {
      val inputStream = new FileInputStream(store)
      keyStore.load(inputStream, passphrase.toCharArray)
    } catch {
      case e: Exception => throw new IllegalArgumentException(s"Cannot load store at location [$store]", e)
    }
    load(ksKeys, keyStore, passphrase)
    load(ksTrust, keyStore, trustPassphrase)
    // KeyManagers decide which key material to use
    val kmf = KeyManagerFactory.getInstance("SunX509")
    kmf.init(ksKeys, keyManagerFactoryPassword.toCharArray)

    // TrustManagers decide whether to allow connections
    val tmf = TrustManagerFactory.getInstance("SunX509")
    tmf.init(ksTrust)

    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(kmf.getKeyManagers, tmf.getTrustManagers, null)
    sslContext
  }

  implicit object Default extends SSLContextDataToSslContext {
    override def apply(data: SSLContextData): SSLContext = getSslContext(data)
  }

}