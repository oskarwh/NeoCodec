# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# NO CHECKED-IN PROTOBUF GENCODE
# source: file.proto
# Protobuf Python Version: 5.28.0
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import runtime_version as _runtime_version
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
_runtime_version.ValidateProtobufRuntimeVersion(
    _runtime_version.Domain.PUBLIC,
    5,
    28,
    0,
    '',
    'file.proto'
)
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


import filetypes_pb2 as filetypes__pb2


DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\nfile.proto\x12\tse.umu.cs\x1a\x0f\x66iletypes.proto\"V\n\x07NeoFile\x12\x0c\n\x04\x66ile\x18\x01 \x01(\x0c\x12\x10\n\x08\x66ileName\x18\x02 \x01(\t\x12+\n\ntargetType\x18\x03 \x01(\x0e\x32\x17.se.umu.cs.NeoFileTypesB\x1d\n\tse.umu.csB\x0eNeoFileMessageP\x01\x62\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'file_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  _globals['DESCRIPTOR']._loaded_options = None
  _globals['DESCRIPTOR']._serialized_options = b'\n\tse.umu.csB\016NeoFileMessageP\001'
  _globals['_NEOFILE']._serialized_start=42
  _globals['_NEOFILE']._serialized_end=128
# @@protoc_insertion_point(module_scope)
