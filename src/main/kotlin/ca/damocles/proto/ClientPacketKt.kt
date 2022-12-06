package ca.damocles.proto

//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: packets.proto

@kotlin.jvm.JvmSynthetic
inline fun clientPacket(block: ClientPacketKt.Dsl.() -> kotlin.Unit): Packets.ClientPacket =
  ClientPacketKt.Dsl._create(Packets.ClientPacket.newBuilder()).apply { block() }._build()
object ClientPacketKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  class Dsl private constructor(
    private val _builder: Packets.ClientPacket.Builder
  ) {
    companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: Packets.ClientPacket.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): Packets.ClientPacket = _builder.build()
  }
  @kotlin.jvm.JvmSynthetic
  inline fun loginPacket(block: LoginPacketKt.Dsl.() -> kotlin.Unit): Packets.ClientPacket.LoginPacket =
    LoginPacketKt.Dsl._create(Packets.ClientPacket.LoginPacket.newBuilder()).apply { block() }._build()
  object LoginPacketKt {
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    @com.google.protobuf.kotlin.ProtoDslMarker
    class Dsl private constructor(
      private val _builder: Packets.ClientPacket.LoginPacket.Builder
    ) {
      companion object {
        @kotlin.jvm.JvmSynthetic
        @kotlin.PublishedApi
        internal fun _create(builder: Packets.ClientPacket.LoginPacket.Builder): Dsl = Dsl(builder)
      }

      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _build(): Packets.ClientPacket.LoginPacket = _builder.build()

      /**
       * <code>string email = 1;</code>
       */
      var email: kotlin.String
        @JvmName("getEmail")
        get() = _builder.getEmail()
        @JvmName("setEmail")
        set(value) {
          _builder.setEmail(value)
        }
      /**
       * <code>string email = 1;</code>
       */
      fun clearEmail() {
        _builder.clearEmail()
      }

      /**
       * <code>string password = 2;</code>
       */
      var password: kotlin.String
        @JvmName("getPassword")
        get() = _builder.getPassword()
        @JvmName("setPassword")
        set(value) {
          _builder.setPassword(value)
        }
      /**
       * <code>string password = 2;</code>
       */
      fun clearPassword() {
        _builder.clearPassword()
      }
    }
  }
  @kotlin.jvm.JvmSynthetic
  inline fun dummyPacket(block: DummyPacketKt.Dsl.() -> kotlin.Unit): Packets.ClientPacket.DummyPacket =
    DummyPacketKt.Dsl._create(Packets.ClientPacket.DummyPacket.newBuilder()).apply { block() }._build()
  object DummyPacketKt {
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    @com.google.protobuf.kotlin.ProtoDslMarker
    class Dsl private constructor(
      private val _builder: Packets.ClientPacket.DummyPacket.Builder
    ) {
      companion object {
        @kotlin.jvm.JvmSynthetic
        @kotlin.PublishedApi
        internal fun _create(builder: Packets.ClientPacket.DummyPacket.Builder): Dsl = Dsl(builder)
      }

      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _build(): Packets.ClientPacket.DummyPacket = _builder.build()

      /**
       * <code>string email = 1;</code>
       */
      var email: kotlin.String
        @JvmName("getEmail")
        get() = _builder.getEmail()
        @JvmName("setEmail")
        set(value) {
          _builder.setEmail(value)
        }
      /**
       * <code>string email = 1;</code>
       */
      fun clearEmail() {
        _builder.clearEmail()
      }

      /**
       * <code>string password = 2;</code>
       */
      var password: kotlin.String
        @JvmName("getPassword")
        get() = _builder.getPassword()
        @JvmName("setPassword")
        set(value) {
          _builder.setPassword(value)
        }
      /**
       * <code>string password = 2;</code>
       */
      fun clearPassword() {
        _builder.clearPassword()
      }
    }
  }
  @kotlin.jvm.JvmSynthetic
  inline fun createAccountPacket(block: CreateAccountPacketKt.Dsl.() -> kotlin.Unit): Packets.ClientPacket.CreateAccountPacket =
    CreateAccountPacketKt.Dsl._create(Packets.ClientPacket.CreateAccountPacket.newBuilder()).apply { block() }._build()
  object CreateAccountPacketKt {
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    @com.google.protobuf.kotlin.ProtoDslMarker
    class Dsl private constructor(
      private val _builder: Packets.ClientPacket.CreateAccountPacket.Builder
    ) {
      companion object {
        @kotlin.jvm.JvmSynthetic
        @kotlin.PublishedApi
        internal fun _create(builder: Packets.ClientPacket.CreateAccountPacket.Builder): Dsl = Dsl(builder)
      }

      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _build(): Packets.ClientPacket.CreateAccountPacket = _builder.build()

      /**
       * <code>string email = 1;</code>
       */
      var email: kotlin.String
        @JvmName("getEmail")
        get() = _builder.getEmail()
        @JvmName("setEmail")
        set(value) {
          _builder.setEmail(value)
        }
      /**
       * <code>string email = 1;</code>
       */
      fun clearEmail() {
        _builder.clearEmail()
      }

      /**
       * <code>string username = 2;</code>
       */
      var username: kotlin.String
        @JvmName("getUsername")
        get() = _builder.getUsername()
        @JvmName("setUsername")
        set(value) {
          _builder.setUsername(value)
        }
      /**
       * <code>string username = 2;</code>
       */
      fun clearUsername() {
        _builder.clearUsername()
      }

      /**
       * <code>string password = 3;</code>
       */
      var password: kotlin.String
        @JvmName("getPassword")
        get() = _builder.getPassword()
        @JvmName("setPassword")
        set(value) {
          _builder.setPassword(value)
        }
      /**
       * <code>string password = 3;</code>
       */
      fun clearPassword() {
        _builder.clearPassword()
      }
    }
  }
  @kotlin.jvm.JvmSynthetic
  inline fun closePacket(block: ClosePacketKt.Dsl.() -> kotlin.Unit): Packets.ClientPacket.ClosePacket =
    ClosePacketKt.Dsl._create(Packets.ClientPacket.ClosePacket.newBuilder()).apply { block() }._build()
  object ClosePacketKt {
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    @com.google.protobuf.kotlin.ProtoDslMarker
    class Dsl private constructor(
      private val _builder: Packets.ClientPacket.ClosePacket.Builder
    ) {
      companion object {
        @kotlin.jvm.JvmSynthetic
        @kotlin.PublishedApi
        internal fun _create(builder: Packets.ClientPacket.ClosePacket.Builder): Dsl = Dsl(builder)
      }

      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _build(): Packets.ClientPacket.ClosePacket = _builder.build()
    }
  }
  @kotlin.jvm.JvmSynthetic
  inline fun pongPacket(block: PongPacketKt.Dsl.() -> kotlin.Unit): Packets.ClientPacket.PongPacket =
    PongPacketKt.Dsl._create(Packets.ClientPacket.PongPacket.newBuilder()).apply { block() }._build()
  object PongPacketKt {
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    @com.google.protobuf.kotlin.ProtoDslMarker
    class Dsl private constructor(
      private val _builder: Packets.ClientPacket.PongPacket.Builder
    ) {
      companion object {
        @kotlin.jvm.JvmSynthetic
        @kotlin.PublishedApi
        internal fun _create(builder: Packets.ClientPacket.PongPacket.Builder): Dsl = Dsl(builder)
      }

      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _build(): Packets.ClientPacket.PongPacket = _builder.build()
    }
  }
}
@kotlin.jvm.JvmSynthetic
inline fun Packets.ClientPacket.copy(block: ClientPacketKt.Dsl.() -> kotlin.Unit): Packets.ClientPacket =
  ClientPacketKt.Dsl._create(this.toBuilder()).apply { block() }._build()
@kotlin.jvm.JvmSynthetic
inline fun Packets.ClientPacket.LoginPacket.copy(block: ClientPacketKt.LoginPacketKt.Dsl.() -> kotlin.Unit): Packets.ClientPacket.LoginPacket =
  ClientPacketKt.LoginPacketKt.Dsl._create(this.toBuilder()).apply { block() }._build()
@kotlin.jvm.JvmSynthetic
inline fun Packets.ClientPacket.DummyPacket.copy(block: ClientPacketKt.DummyPacketKt.Dsl.() -> kotlin.Unit): Packets.ClientPacket.DummyPacket =
  ClientPacketKt.DummyPacketKt.Dsl._create(this.toBuilder()).apply { block() }._build()
@kotlin.jvm.JvmSynthetic
inline fun Packets.ClientPacket.CreateAccountPacket.copy(block: ClientPacketKt.CreateAccountPacketKt.Dsl.() -> kotlin.Unit): Packets.ClientPacket.CreateAccountPacket =
  ClientPacketKt.CreateAccountPacketKt.Dsl._create(this.toBuilder()).apply { block() }._build()
@kotlin.jvm.JvmSynthetic
inline fun Packets.ClientPacket.ClosePacket.copy(block: ClientPacketKt.ClosePacketKt.Dsl.() -> kotlin.Unit): Packets.ClientPacket.ClosePacket =
  ClientPacketKt.ClosePacketKt.Dsl._create(this.toBuilder()).apply { block() }._build()
@kotlin.jvm.JvmSynthetic
inline fun Packets.ClientPacket.PongPacket.copy(block: ClientPacketKt.PongPacketKt.Dsl.() -> kotlin.Unit): Packets.ClientPacket.PongPacket =
  ClientPacketKt.PongPacketKt.Dsl._create(this.toBuilder()).apply { block() }._build()
