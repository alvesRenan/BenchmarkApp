// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: fibonacci.proto

package grpcfibonacci;

public final class FibonacciOuterClass {
  private FibonacciOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_grpcfibonacci_Position_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_grpcfibonacci_Position_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_grpcfibonacci_Value_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_grpcfibonacci_Value_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017fibonacci.proto\022\rgrpcfibonacci\"\032\n\010Posi" +
      "tion\022\016\n\006number\030\001 \001(\005\"\027\n\005Value\022\016\n\006number\030" +
      "\001 \001(\0052G\n\tFibonacci\022:\n\007compute\022\027.grpcfibo" +
      "nacci.Position\032\024.grpcfibonacci.Value\"\000B\002" +
      "P\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_grpcfibonacci_Position_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_grpcfibonacci_Position_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_grpcfibonacci_Position_descriptor,
        new java.lang.String[] { "Number", });
    internal_static_grpcfibonacci_Value_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_grpcfibonacci_Value_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_grpcfibonacci_Value_descriptor,
        new java.lang.String[] { "Number", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}